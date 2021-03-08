package jdbc.builder;

import java.util.Map;
/**
 * 
 * @author danping tang
 * @version March 6 2021
 */
public class MySQLURLBuilder extends JDBCURLBuilder{
	
	public MySQLURLBuilder() {
		super();
		setDB("mysql");	
	}

	@Override
	public String getURL() {
		StringBuilder builder = new StringBuilder();//Create a StringBuilder object.
		builder.append( String.format( "%s:%s://%s:%s/%s?",
				(JDBC),
				(dbType),
				(hostAddress),
				(portNumber),
				(catalogName)));
	
		if(!properties.isEmpty()) {
			for (Map.Entry<String, String> pro : properties.entrySet()) {
				builder.append(pro.getKey());//get string of the key v
				builder.append("=");
				builder.append(pro.getValue());//get the string of the value
				builder.append("&");
			}	
        builder.setLength(builder.length() - 1);//set string length not to display the &
		}
		
		return builder.toString();
		
	}

}
