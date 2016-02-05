package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            ResultSet rs = st.executeQuery("select count(*) as count "
                    + "from information_schema.tables "
                    + "where "
                    + "(table_schema = 'adiiu' and table_name = 'messages') or "
                    + "(table_schema = 'adiiu' and table_name = 'users')  or "
                    + "(table_schema = 'adiiu' and table_name = 'diesAmbPes');");

            if (rs.next()) {
                int num = rs.getInt("count");

                return num != 3;
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
            URL sql = new URL("http://127.0.0.1:8080/ADI/inc/install.sql");
            BufferedReader buff = new BufferedReader(new InputStreamReader(sql.openStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = buff.readLine()) != null) {
                sb.append(line);
            }
            buff.close();
            String[] sqlInst = sb.toString().split(";");
            
            Statement st = con.getConection().createStatement();
            for (String inst : sqlInst) {
                if (!inst.trim().equals("")) {
                    st.executeUpdate(inst);
                }
            }

        
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
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
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select count(*) as count from users "
                    + "where user_name = '" + userName + "' "
                    + "and hashed_pass = "
                    + "sha2(concat(creation_time, '" + password + "'), 512) "
                    + "collate 'latin1_spanish_ci';";
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                int total = rs.getInt("count");
                return 1 == total;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            con.close();
        }
        return false;
    }

}
