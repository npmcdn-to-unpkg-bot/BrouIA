package database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBActions {
    public ArrayList <String> getTendencias() {
        DBConnection con = new DBConnection();
        ArrayList <String> res = new ArrayList <String> ();
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            ResultSet rs = st.executeQuery("select * from sm_tendencias;");
            String aux;
            while (rs.next()) {
                aux = rs.getString("nom_tendencia");
                aux = aux + "##ltim##" + rs.getInt("total_ocurrencias");
                res.add(aux);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            con.close();
        }
        return res;
    }
    
    
    public boolean installIsNeed() {
        DBConnection con = new DBConnection();
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            
            ResultSet rs = st.executeQuery("select count(*) as count from information_schema.tables where"
                    + " table_name = 'users' and table_schema = 'adiiu'");
            
            if (rs.next()) {
                int num = rs.getInt("count");
                
                return num <= 0;
            }
            
        } catch (Exception ex) {
            
        } finally {
            con.close();
        }
        
        return true;
    }
    
    public void installOrResetAll() {
        DBConnection con = new DBConnection();
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            
            //users
            boolean dropOk = st.execute("drop table if exists users;");
            boolean createOk = st.execute("create table `users` ( "
                    + "name varchar(100) primary key, "
                    + "u_time int(10), "
                    + "hashed_pass varchar(128)"
                    + ");");
        } catch (Exception ex) {
        } finally {
            con.close();
        }
    }
    
    public boolean createUser(String userName, String password) {
        long unixTime = System.currentTimeMillis() / 1000L;
        
        DBConnection con = new DBConnection();
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            st.execute("insert into users values ('"
                    + userName + "',"
                    + unixTime + ","
                    + "sha2(concat('" + unixTime + "', '" + password + "'), 512))");
            return true;
        } catch (Exception ex) {
        } finally {
            con.close();
        }
        return false;
    }
    
    public boolean logInUser(String userName, String password) {
        long unixTime = System.currentTimeMillis() / 1000L;
        
        DBConnection con = new DBConnection();
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select count(*) as count form users where "
                    + "name = " + userName + " and "
                    + "hashed_pass = sha2(concat(u_time, '" + password + "'), 512);";
            ResultSet rs = st.executeQuery("insert into users values ('"
                    + userName + "',"
                    + unixTime + ","
                    + "sha2(concat('" + unixTime + "', '" + password + "'), 512))");
            
            if (rs.next()) {
                return 1 == rs.getInt("count");
            }
        } catch (Exception ex) {
        } finally {
            con.close();
        }
        return false;
    }
    
}
