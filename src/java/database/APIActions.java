
package database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class APIActions {
    
    @GET
    @Path("illes")
    public String getIlles() {
        DBConnection con = new DBConnection();
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            //ResultSet rs = st.executeQuery("select isla from sm_procesados group by isla");
            String query = "select isla, municipio from adiiu.sm_procesados group by municipio order by isla;";
            ResultSet rs = st.executeQuery(query);
            
            ArrayList<String[]> illes = new ArrayList<>();
            while (rs.next()) {
                String[] info = new String[2];
                info[0] = rs.getString("isla");
                info[1] = rs.getString("municipio");
                illes.add(info);
            }
            Collections.sort(illes, new Comparator<String[]>() {
                public int compare(String[] s1, String[] s2) {
                    return s1[0].compareTo(s2[0]);
                }
            });
            
            
            //generam es JSON
            StringBuilder builder = new StringBuilder();  
            
            builder.append("{\"illes\":[");
            String illaActual = "_";
            for (String[] illa : illes) {
                String illaLinea = illa[0].isEmpty()? "NC": illa[0];
                String municipiLinea = illa[1].isEmpty()? "NC": illa[1];
                
                if (illaActual.equals(illaLinea)){
                    builder.append("{\"nom_municipi\": \"");
                    builder.append(municipiLinea);
                    builder.append("\"},");
                }
                else {
                    if (!"_".equals(illaActual)) {
                        builder.append("]},");
                    }
                    
                    builder.append("{ \"nom_illa\" : \"");
                    builder.append(illaLinea);
                    builder.append("\", \"municipis\":[");
                    builder.append("{\"nom_municipi\": \"");
                    builder.append(municipiLinea);
                    builder.append("\"},");
                    
                    illaActual = illaLinea;
                }
            }
            builder.append("]}]}");
            
            
            String res = builder.toString();
            return builder.toString();
        } catch(Exception ex) {
        } finally {
            con.close();
        }
        
        return "";
    }
    
}
