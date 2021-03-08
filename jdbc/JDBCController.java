package jdbc;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.builder.JDBCURLBuilder;
/**
 * 
 * @author danping tang
 * @version March 6 2021
 */
public class JDBCController implements AutoCloseable {
	
	private JDBCURLBuilder builder;
	private JDBCModel model;
	private StringProperty tableInUse;
	private ObservableList<String> tableNamesList;
	
	
	public JDBCController() {
		//initialize the tableNamesList with FXCollections
		this.tableNamesList = FXCollections.observableArrayList(); 
		//Initialize model and tableInUse
		this.model = new JDBCModel();
     	this.tableInUse = new SimpleStringProperty();
     	//Add a listener and call method model.getAndInitializeColumnNames,pass newVlue to it
     	tableInUse.addListener( ( value, oldValue, newValue) -> {
     		try {
				model.getAndInitializeColumnNames(newValue);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	});
		
	}
	public StringProperty tableInUseProperty() {
		return this.tableInUse;
	}
	
	//	JDBCController setURLBuilder( JDBCURLBuilder builder);
	public JDBCController setURLBuilder( JDBCURLBuilder builder) {
      this.builder = builder;
      return this;
		
	}
	//	JDBCController setDataBase( String address, String port, String catalog);
	public JDBCController setDataBase( String address, String port, String catalog) {
		builder.setAddress(address);
		builder.setPort(port);
		builder.setCatalog(catalog);
		return this;
	}
	//	JDBCController addConnectionURLProperty( String key, String value);
	public JDBCController addConnectionURLProperty( String key, String value) {
		builder.addURLProperty(key, value);
		return this;
		
	}
	//	JDBCController setCredentials( String user, String pass);
	public JDBCController setCredentials( String user, String pass) {
		model.setCredentials(user, pass);
		return this;
	}
	//	JDBCController connect() throws SQLException;
	public JDBCController connect() throws SQLException{
		model.connectTo(builder.getURL());
		return this;
	
	}
	//	boolean isConnected() throws SQLException;
	public boolean isConnected() throws SQLException{
		return model.isConnected();
		
	}
	//	List< String> getColumnNames() throws SQLException;
	public List< String> getColumnNames() throws SQLException {
		
		return model.getAndInitializeColumnNames(tableInUse.getValue());
	}
	//	ObservableList< String> getTableNames() throws SQLException;
	public ObservableList< String> getTableNames() throws SQLException{
		if(model.isConnected()){
			this.tableNamesList.clear();
			this.tableNamesList.addAll(model.getAndInitializeTableNames());
			
		}
		return this.tableNamesList;
		
	}
	//	List< List< Object>> getAll() throws SQLException;
	public List< List< Object>> getAll() throws SQLException{
		
		return model.getAll(tableInUse.getValue());
		
	}
	//	List< List< Object>> search( String searchTerm) throws SQLException;
	public List< List< Object>> search( String searchTerm) throws SQLException{
		return model.search(tableInUse.getValue(), searchTerm);
		
	}
	//	void close() throws Exception;
	@Override
	public void close() throws Exception {
	model.close();	
	}

}
