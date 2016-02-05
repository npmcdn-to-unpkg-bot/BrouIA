package RESTFull.endpoints;

import database.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/data")
public class DataEndpoint {

    @GET
    @Path("/illes")
    public Response getIllesData() {
        ArrayList<String[]> illes = getIlles();

        if (illes.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{\"illes\":[");
        String illaActual = "_";
        for (String[] illa : illes) {
            String illaLinea = illa[0].isEmpty() ? "NC" : illa[0];
            String municipiLinea = illa[1].isEmpty() ? "NC" : illa[1];

            if (illaActual.equals(illaLinea)) {
                builder.append("{\"nom_municipi\": \"");
                builder.append(municipiLinea);
                builder.append("\"},");
            } else {
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

        return Response.ok(builder.toString()).build();
    }

    @GET
    @Path("/hours/{isla}/{municipio}")
    public Response getMunicipioHoursChartData(@PathParam("isla") String isla,
            @PathParam("municipio") String municipio) {
        ArrayList<float[]> times = getHoursByMunicipio(municipio, isla);
        if (times.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        StringBuilder jsonStgr = new StringBuilder();
        jsonStgr.append("{\"times\":[");
        for (float[] time : times) {
            jsonStgr.append("{\"desde\":")
                    .append(time[0])
                    .append(",\"total\":")
                    .append(time[1])
                    .append("},");
        }
        jsonStgr.append("]}");

        return Response.ok(jsonStgr.toString()).build();
    }

    @GET
    @Path("/days/{isla}/{municipio}")
    public Response getMunicipioDaysChartData(@PathParam("isla") String isla,
            @PathParam("municipio") String municipio) {
        ArrayList<int[]> dies = getDiesByMunicipio(municipio, isla);
        if (dies.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        StringBuilder jsonStgr = new StringBuilder();
        jsonStgr.append("{\"dies\":[");
        for (int[] dia : dies) {
            jsonStgr.append("{\"total\":")
                    .append(dia[0])
                    .append(",\"dia_num\":")
                    .append(dia[1])
                    .append("},");
        }
        jsonStgr.append("]}");

        return Response.ok(jsonStgr.toString()).build();
    }

    /* PRIVATE METHODS */
    private ArrayList<String[]> getIlles() {
        ArrayList<String[]> illes = new ArrayList<>();
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select isla, municipio "
                    + "from sm_procesados group by municipio order by isla;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String[] info = new String[2];
                info[0] = rs.getString("isla");
                info[1] = rs.getString("municipio");
                illes.add(info);
            }
            Collections.sort(
                    illes,
                    (String[] s1, String[] s2) -> s1[0].compareTo(s2[0])
            );
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return illes;
    }

    private ArrayList<float[]> getHoursByMunicipio(String municipio, String isla) {
        DBConnection con = new DBConnection();
        ArrayList<float[]> times = new ArrayList();

        try {
            con.open();
            Statement st = con.getConection().createStatement();

            String query = "select "
                    + "0.2 * ( (hora + (minuto / 60)) div 0.2) as desde, "
                    + "0.2 * ( (hora + (minuto / 60)) div 0.2) + 0.19999999 as fins, "
                    + "count(*) as total "
                    + "from sm_procesados "
                    + "where municipio = '" + municipio + "' and isla = '" + isla + "' "
                    + "group by (hora + (minuto / 60)) div 0.2 "
                    + "order by hora, minuto;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                float[] time = new float[2];
                time[0] = rs.getFloat("desde");
                time[1] = rs.getFloat("total");
                times.add(time);
            }

        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return times;
    }

    private ArrayList<int[]> getDiesByMunicipio(String municipio, String isla) {
        DBConnection con = new DBConnection();
        ArrayList<int[]> dies = new ArrayList();
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select count(*) as total, (pes - 1) as dia_num "
                    + "from diesambpes as dap "
                    + "left join sm_procesados as sp "
                    + "on sp.dia_sem = dap.dia "
                    + "where municipio = '" + municipio + "' and "
                    + "isla = '" + isla + "' "
                    + "group by dap.dia order by dap.pes;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int[] dia = new int[2];
                dia[0] = rs.getInt("total");
                dia[1] = rs.getInt("dia_num");
                dies.add(dia);
            }
            
        } catch (Exception ex) {
        } finally {
            con.close();
        }
        
        return dies;
    }
}
