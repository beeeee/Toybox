import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
*	This class models a very simple data source you can use when you
*	need to connect to a database.  To use, first call the init() method
*   and pass it the name of a properties file.  The properties file
*	contains the driver name and URL, and the user name and password used
*	to access the database.  For example:
*
*	<p>Example: <font face="Courier New">SimpleDataSource.init("database.properties");</font></p>
*
*	<p>If you aren't using a user name and password, leave the line empty 
*	after the = sign, e.g. <font face="Courier New">jdbc.username=</font></p>
*
*	Next, call the getConnection() method to establish a connection with
*	the database:<br>
*	<font face="Courier New">Connection conn = SimpleDataSource.getConnection();</font>
*
*	@author Wendi Jollymore, Summer 05
*/
public class SimpleDataSource 
{
	// the location, username, and password of the database
	private static String url;
	private static String username;
	private static String password;

	/**
	*	Registers the driver so that a connection can be made.
	*	@param fileName the name of the file that contains the database properties
	*	@throws IOException if there is a problem with the properties file
	*	@throws ClassNotFoundException if the driver classes are missing
	*/
	public static void init(String fileName) throws IOException, ClassNotFoundException
	{
		// create a properties object
		Properties props = new Properties();
		// open an input stream from the properties file
		FileInputStream in = new FileInputStream(fileName);
		// load the properties from the file to the properties object
		props.load(in);

		// get the properties and store in string variables
		String driver = props.getProperty("jdbc.driver");
		url = props.getProperty("jdbc.url");
		username = props.getProperty("jdbc.username");
		password = props.getProperty("jdbc.password");
		
		// register the driver
		Class.forName(driver);
		in.close();
	}
	
	/**
	*	Registers the driver so that a connection can be made.
	*	@param driver the name of the driver
	*	@param u the url where the driver is located
	*	@param n the username used to log into the database
	*	@param p the password used to log into the database
	*	@throws ClassNotFoundException if the driver classes are missing
	*/
	public static void init(String driver, String u, String n, String p) throws ClassNotFoundException
	{
		// get the properties and store in string variables
		url = u;
		username = n;
		password = p;
		
		// register the driver
		Class.forName(driver);
	}

	/**
	*	Establishes a connection with the data source specified in the
	*	database properties file.
	*	@return a Connection object that is connected to the database
	*	@throws SQLException if there is any problem making the connection
	*/
	public static Connection getConnection() throws SQLException
	{
		// make the connection to the database using the specified
		// username and password from the properties file
		return DriverManager.getConnection(url, username, password);
	}
}

