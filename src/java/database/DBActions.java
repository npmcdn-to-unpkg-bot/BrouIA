package database;

import Models.Missatge;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBActions {

    public ArrayList<String> getTendencias() {
        DBConnection con = new DBConnection();
        ArrayList<String> res = new ArrayList<String>();
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
                    + "( table_name = 'users' or table_name= 'messages') and table_schema = 'adiiu'");

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
            st.execute("drop table if exists users;");
            st.execute("create table `users` ( "
                    + "name varchar(100) primary key, "
                    + "u_time int(10), "
                    + "hashed_pass varchar(128)"
                    + ");");

            st.execute("drop table if exists messages;");
            st.execute("create table `messages` ( "
                    + "id byte(16) primary key,"
                    + "name_emisor varchar(100), "
                    + "name_receptor varchar(100), "
                    + "message varchar(10000), "
                    + "u_time int(10) "
                    + ");");
        } catch (Exception ex) {
        } finally {
            con.close();
        }
    }

    public boolean createMesssage(String transmitter, String addressee, String message) {

        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            st.execute("insert into users values ('"
                    + "uuid(),'"
                    + transmitter + "','"
                    + addressee + "','"
                    + message + "',"
                    + "unix_timestamp(),");
            return true;
        } catch (Exception ex) {
        } finally {
            con.close();
        }
        return false;
    }

    public Missatge[] getMesssage(String addressee) {

        DBConnection con = new DBConnection();
        ArrayList<Missatge> msAL=new ArrayList<>();
        Missatge arrayMissatges[];
        Missatge ms= new Missatge();
//        String name_emisor;
//        String message;
//        long u_time;
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            ResultSet rs = st.executeQuery("SELECT name_emisor, message, u_time FROM messages WHERE name_receptor= '" + addressee + "' ORDER BY u_time DESC;");

            while (rs.next()) {   
            ms=new Missatge(rs.getString("message"),rs.getString("name_emisor"), rs.getLong("u_time"));
            msAL.add(ms);
            }
            
            arrayMissatges=(Missatge[])msAL.toArray();            
            
            return arrayMissatges;
        } catch (Exception ex) {
        } finally {
            con.close();
        }
        return null;
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
