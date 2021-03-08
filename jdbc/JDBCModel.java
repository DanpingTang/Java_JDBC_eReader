package jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
/**
 * 
 * @author danping tang
 * @version March 6 2021
 */
public class JDBCModel {
	private List<String> columnNames;
	private List<String> tableNames;
	private Connection connection;
    private String user;
    private String pass;
    
 JDBCModel() {
     //initialize columnNames and tableNames
	 this.columnNames = new LinkedList<>();
	 this.tableNames = new LinkedList<>(); 
    }
	
	public void setCredentials(String user , String pass ) {
		this.user = user;
		this.pass = pass;
	}
	
	private void checkConnectionIsValid() {
		try {
			if (connection == null || connection.isClosed()) {	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void checkTableNameAndColumnsAreValid(String table) throws SQLException {
		
		Objects.requireNonNull( table, "table name cannot be null");
		table = table.trim();
		if (tableNames.isEmpty()) {
	        getAndInitializeTableNames();      
	    }
		
		if (columnNames.isEmpty()) {
	        getAndInitializeColumnNames(table);
	    }
		
		if (table.isEmpty() || !tableNames.contains(table)) {
		    throw new IllegalArgumentException("Table Name = \"" + table + "\"is not valid");
		}
	}
	
	public void connectTo(String url) throws SQLException {
		if (isConnected()) {
			close();
		}
		connection = DriverManager.getConnection(url,user,pass);
		
	}
	
	public boolean isConnected() throws SQLException {	
		if (connection!= null) {
			return true;
		}
		return false;
	}
	
	
	public List<String> getAndInitializeColumnNames(String table) throws SQLException{
		
		checkConnectionIsValid();
		 columnNames.clear();
		 DatabaseMetaData dbMeta =connection.getMetaData();
		try (ResultSet rs = dbMeta.getColumns( connection.getCatalog(), null, table, null)){
			while(rs.next()) {
				columnNames.add( rs.getString( "COLUMN_NAME"));
			}
		} 
		 List<String> list = Collections.unmodifiableList(columnNames);
	 
		 return list;
	}
	
	public List<String> getAndInitializeTableNames() throws SQLException{
		checkConnectionIsValid();
		tableNames.clear();
		DatabaseMetaData dbMeta =connection.getMetaData();
		try (ResultSet rs = dbMeta.getTables( connection.getCatalog(), null, null, new String[]{ "TABLE" })){
			while(rs.next()) {
				tableNames.add( rs.getString( "TABLE_NAME"));
			}
		} 
		 List<String> list = Collections.unmodifiableList(tableNames);
	 
		 return list;
		
	}
	
	public List<List<Object>> getAll(String table) throws SQLException{
		
		return search(table, "");
	}
	
	public List<List<Object>> search(String table, String searthTerm) throws SQLException {
		checkConnectionIsValid();
		checkTableNameAndColumnsAreValid(table);//Check table and column names are valid.
		List<List<Object>> list = new LinkedList<List<Object>>();

		//store query results
		String rs = buildSQLSearchQuery(table,true);
		
		//PreparedStatement is created
		try( PreparedStatement statement = connection.prepareStatement(rs)){
			if (searthTerm!=null) {
				searthTerm = String.format( "%%%s%%", searthTerm);
				for(int i = 0; i < columnNames.size(); i ++) {
					statement.setObject(i+1, searthTerm);
				}
			}
			extractRowsFromResultSet(statement,list);		
		}
		return list;
	}
	
	private String buildSQLSearchQuery(String table, boolean withParameters) {
		//Create SQL query result: "select * from (tableName) where (columnName) like ? or user like ? or pass like ?"
		
		StringBuilder builder = new StringBuilder("select * from ");
		builder.append(table);
		//check if there are no parameters
		if (!withParameters) {
			return builder.toString();//return the String in StringBuilder
		}else {
			builder.append(" where ");
			//loop to append all the column names
			for (String name: columnNames) {
				builder.append( name + " like ? or ");
			}
		}
		builder.setLength(builder.length() - 3);//set length not to display delete the last 3 spaces ("or ")
		return builder.toString();
	}
	
	private void extractRowsFromResultSet(PreparedStatement ps, List<List<Object>> list ) throws SQLException {
		try (ResultSet result = ps.executeQuery()){
			//executes the query of PreparedStatemen and store the result in resut
			while(result.next()) {
				//create a new list called row and add to it the content of each row
				List<Object> row = new LinkedList<Object>();
				//loop to get add all column names
				 for(String colomnString : columnNames)
		            {
		                row.add(result.getObject(colomnString));
		            }
		            list.add(row);
			}
		} 
	}
	
	public void close() {
		if (connection != null) {
	
		  try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
