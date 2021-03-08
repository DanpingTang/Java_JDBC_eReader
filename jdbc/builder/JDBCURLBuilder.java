package jdbc.builder;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author danping tang
 * @version March 6 2021
 *
 */
public abstract class JDBCURLBuilder {
	protected static final String JDBC = "jdbc";
	protected Map<String, String> properties;
	protected String dbType;
	protected int portNumber;
	protected String hostAddress;
	protected String catalogName;
	
	public JDBCURLBuilder() {
		properties = new HashMap<String, String>();
	}
	
	//	void setPort( String port);
	public void setPort( String port) {
		//Error checking: if port is null throw a null pointer exception
		if (port == null) {
			throw new NullPointerException();
		}
		setPort(Integer.parseInt(port));// change string port to int and call setport(int port) method
		
	} 
	//	void setPort( int port);
	public void setPort( int port) {
		//Error checking: if port is negative throw an illegal argument exception
		if (port <0 ) {
			throw new IllegalArgumentException();
		}
		this.portNumber = port;
		
	}
	//	void setAddress( String address);
	public void setAddress( String address) {
		this.hostAddress = address;
		
	}
	//	void setCatalog( String catalog);
	public void setCatalog( String catalog) {
		this.catalogName = catalog;	
	}
	//	void addURLProperty( String key, String value);
	public void addURLProperty( String key, String value) {
		properties.put(key, value);
		
	}
	protected void setDB(String db) {
		this.dbType = db;
	}
	//	String getURL();
	public  abstract  String getURL(); 
		
}