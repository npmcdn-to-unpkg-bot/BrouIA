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
}
