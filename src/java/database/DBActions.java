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
    
    
}
