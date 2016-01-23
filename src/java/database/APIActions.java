
package database;

import java.sql.ResultSet;
import java.sql.Statement;
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
            ResultSet rs = st.executeQuery("select isla from sm_procesados group by isla");
            
            StringBuilder builder = new StringBuilder();
            
            //generam es JSON
            builder.append("{\"illes\":[");
            while (rs.next()) {
                builder.append("{\"nom_illa\":\"");
                String nom = rs.getString("isla");
                if (nom.isEmpty()){
                    builder.append("NC");
                } else {
                    builder.append(nom);
                }
                builder.append("\"},");
            }
            builder.append("]}");
            
            return builder.toString();
        } catch(Exception ex) {
        } finally {
            con.close();
        }
        
        return "";
    }
    
}
