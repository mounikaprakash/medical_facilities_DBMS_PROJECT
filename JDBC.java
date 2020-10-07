
import java.sql.*;

public class JDBC {

    static final String jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";

    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String user = "lpolice";	// For example, "jsmith" 
            String passwd = "200260009";	// Your 9 digit student ID number or password 
            Connection conn = null;

            conn = DriverManager.getConnection(jdbcURL, user, passwd);
    		return conn;
        } 
        catch(Throwable oops) {
            oops.printStackTrace();
        }
        return null;
    }

    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }
}



