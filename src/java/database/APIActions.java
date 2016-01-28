package database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

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

            String res = builder.toString();
            return builder.toString();
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return "fail";
    }

    @GET
    @Path("municipioByDays")
    public String getDiesByMinucipi(@QueryParam("municipio") String municipio, @QueryParam("isla") String isla) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String where = "where ";
            if (municipio != null && isla == null) {
                where += "municipio = '" + municipio + "'";
            } else if (municipio == null && isla != null) {
                where += "isla = '" + isla + "'";
            } else {
                where += "municipio = '" + municipio + "' and isla = '" + isla + "'";
            }
            String query = "select count(*) as total, dia, pes "
                    + "from diesambpes as dap "
                    + "left join sm_procesados as sp on sp.dia_sem = dap.dia "
                    + where + " "
                    + "group by dap.dia order by dap.pes;";
            ResultSet rs = st.executeQuery(query);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"dies\":[");
            while (rs.next()) {
                sb.append("{\"total\":");
                sb.append(rs.getInt("total"));
                sb.append(",\"dia\":\"");
                sb.append(rs.getString("dia"));
                sb.append("\",\"pes\":");
                sb.append(rs.getInt("pes"));
                sb.append("},");
            }
            sb.append("]}");

            return sb.toString();
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return "fail";
    }

    @GET
    @Path("municipioByMonth")
    public String getMesosByMinucipi(@QueryParam("municipio") String municipio, @QueryParam("isla") String isla) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();

            String where = "where ";
            if (municipio != null && isla == null) {
                where += "municipio = '" + municipio + "'";
            } else if (municipio == null && isla != null) {
                where += "isla = '" + isla + "'";
            } else {
                where += "municipio = '" + municipio + "' and isla = '" + isla + "'";
            }

            String query = "select count(*) as total, mes_nom as mes, mes_num as num "
                    + "from sm_procesados "
                    + where + " "
                    + "group by mes_nom order by mes_num;";
            ResultSet rs = st.executeQuery(query);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"mesos\":[");
            while (rs.next()) {
                sb.append("{\"total\":");
                sb.append(rs.getInt("total"));
                sb.append(",\"mes\":\"");
                sb.append(rs.getString("mes"));
                sb.append("\",");
                sb.append("\"num\":");
                sb.append(rs.getInt("num"));
                sb.append("},");
            }
            sb.append("]}");

            return sb.toString();
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return "fail";
    }

    @GET
    @Path("municipioByTime")
    public String getTempsByMinucipi(@QueryParam("municipio") String municipio, @QueryParam("isla") String isla) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();

            String where = "where ";
            if (municipio != null && isla == null) {
                where += "municipio = '" + municipio + "'";
            } else if (municipio == null && isla != null) {
                where += "isla = '" + isla + "'";
            } else {
                where += "municipio = '" + municipio + "' and isla = '" + isla + "'";
            }

            String query = "select "
                    + "0.2 * ( (hora + (minuto / 60)) div 0.2) as desde, "
                    + "0.2 * ( (hora + (minuto / 60)) div 0.2) + 0.19999999 as fins, "
                    + "count(*) as total "
                    + "from sm_procesados "
                    + where + " "
                    + "group by (hora + (minuto / 60)) div 0.2 "
                    + "order by hora, minuto;";
            ResultSet rs = st.executeQuery(query);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"times\":[");
            while (rs.next()) {
                sb.append("{\"desde\":");
                sb.append(rs.getFloat("desde"));
                sb.append(",\"fins\":");
                sb.append(rs.getFloat("fins"));
                sb.append(",");
                sb.append("\"total\":");
                sb.append(rs.getFloat("total"));
                sb.append("},");
            }
            sb.append("]}");

            return sb.toString();
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return "fail";
    }
}
