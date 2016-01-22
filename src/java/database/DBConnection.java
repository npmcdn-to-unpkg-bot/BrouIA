
package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private Connection con;

    public DBConnection() {
        con = null;
    }

    public void open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"
                    + DBProperties.host + ":" + DBProperties.port +
                    "/" + DBProperties.db, DBProperties.user, DBProperties.pass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void close() {
        try {
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Connection getConection() {
        return con;
    }
}
