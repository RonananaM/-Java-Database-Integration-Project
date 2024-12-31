//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.*;

public class DataBase {
    // Assign common pieces of SQL statements to variables for ease of use throughout program
    final String DBServer = "localhost";
    final String DBPort = "3306";
    protected static final String DBSchema = "job_tracking";
    final String DBOptions ="autoReconnect=true&useSSL=false";
    final String DBUser = "root";
    final String DBPassWd = "root";
    protected static final String PEOPLE_TABLE = "profile_data";
    protected static final String POSTINGS_TABLE = "posting_data";

    // Declare java.sql connection and statement objects
    public Connection con;
    public Statement stmt;


    public DataBase (){
        try {
            // Create connection and statement objects
            this.con = DriverManager.getConnection("jdbc:mysql://"+DBServer+":"+DBPort+"/"
                    +DBSchema+"?"+DBOptions, DBUser, DBPassWd);
            this.stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            // For future reference, EXECUTE UPDATE command returns number of rows touched
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}  // END DataBase class
