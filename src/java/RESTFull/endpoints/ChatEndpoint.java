package RESTFull.endpoints;

import Models.ChatMessage;
import RESTFull.Authorize;
import database.DBConnection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

@Authorize
@Path("/chat")
public class ChatEndpoint {

    private final static List<ChatMessage> CHAT
            = Collections.synchronizedList(new ArrayList<ChatMessage>());

    @GET
    @Path("/read/{friend}")
    public Response getMyself(@PathParam("friend") String friend) {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                Writer writer = new BufferedWriter(new OutputStreamWriter(output));

                long currentTime = System.currentTimeMillis() / 1000L;
                for (ChatMessage msg : CHAT) {
                    if (!msg.isRead() && msg.getFrom().equals(friend) && msg.getTime() >= currentTime) {
                        writer.write(msg.toString() + "\n");
                        markAsReadMessage(msg);
                        msg.ReadMessage();
                    }
                }
                writer.flush();
            }
        };
        return Response.ok(stream).build();
    }

    @GET
    @Path("/past")
    public Response getPast(@QueryParam("friend") String friend,
            @Context SecurityContext securityContext) {
        String myself = securityContext.getUserPrincipal().getName();

        ArrayList<ChatMessage> messages = (ArrayList<ChatMessage>) getMessagesFromUserDB(myself, friend);
        StringBuilder sb = new StringBuilder();
        sb.append("{\"messages\": [ ");
        for (ChatMessage cm : messages) {
            sb.append(cm).append(",");
        }
        sb.append("]}");
        
        markAsReadAllMessages(myself, friend);
        
        return Response.ok(sb.toString()).build();
    }

    @GET
    @Path("/users")
    public Response getUsers(@Context SecurityContext securityContext) {
        DBConnection con = new DBConnection();
        String myself = securityContext.getUserPrincipal().getName();
        ArrayList<String> users = new ArrayList();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select user_name as name from users;";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                if (!name.equals(myself)) {
                    users.add(rs.getString("name"));
                }
            }

        } catch (Exception ex) {
        } finally {
            con.close();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\"users\": [");
        for (String name : users) {
            sb.append("{\"name\":\"");
            sb.append(name);
            sb.append("\"},");
        }
        sb.append("]}");

        return Response.ok(sb.toString()).build();
    }

    @GET
    @Path("/unreads")
    public Response getUnreads(@Context SecurityContext securityContext) {
        DBConnection con = new DBConnection();
        String myself = securityContext.getUserPrincipal().getName();
        
        StringBuilder jsonUnrd = new StringBuilder();
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select user_from, count(*) as total "
                    + "from messages  where user_to = '" + myself + "' "
                    + "and is_readed = false group by user_from;";
            ResultSet rs = st.executeQuery(query);
            jsonUnrd.append("{\"unreads\": [");
            while (rs.next()) {
                jsonUnrd.append("{\"name\":\"")
                        .append(rs.getString("user_from"))
                        .append("\", \"total\":")
                        .append(rs.getInt("total"))
                        .append("},");
            }
            jsonUnrd.append("]}");
            
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return Response.ok(jsonUnrd.toString()).build();
    }

    @POST
    @Path("/send")
    public Response send(@HeaderParam("to") String to, 
            @HeaderParam("msg") String msg, @Context SecurityContext securityContext) {
        
        String myself = securityContext.getUserPrincipal().getName();
        
        final ChatMessage cm = new ChatMessage(to, myself, msg);
        synchronized (CHAT) {
            CHAT.add(cm);
        }
        postMessageIntoDB(cm);
        return Response.status(Response.Status.CREATED).entity(cm.toString()).build();
    }

    private void postMessageIntoDB(ChatMessage cm) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "insert into messages values ("
                    + "'" + cm.getTo() + "', "
                    + "'" + cm.getFrom() + "', "
                    + "'" + cm.getMessage() + "', "
                    + cm.isRead() + ", "
                    + cm.getTime() + ");";
            st.execute(query);

        } catch (Exception ex) {
        } finally {
            con.close();
        }
    }
    
    private void markAsReadMessage(ChatMessage cm) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "update messages set is_readed = true "
                    + "where "
                    + "user_to = '" + cm.getTo() + "' and "
                    + "user_from = '" + cm.getFrom()+ "' and "
                    + "message = '" + cm.getMessage() + "' and "
                    + "ceation_time = " + cm.getTime() + ";";
            st.execute(query);

        } catch (Exception ex) {
        } finally {
            con.close();
        }
    }
    
    private void markAsReadAllMessages(String myself, String friend) {
        DBConnection con = new DBConnection();
        
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "update messages set is_readed = true "
                    + "where "
                    + "user_to = '" + myself + "' and "
                    + "user_from = '" + friend+ "';";
            st.execute(query);

        } catch (Exception ex) {
        } finally {
            con.close();
        }
    }

    private List<ChatMessage> getMessagesFromUserDB(String myself, String friend) {
        ArrayList<ChatMessage> list = new ArrayList();
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select * from messages "
                    + "where "
                    + "(user_to = '" + myself + "' and user_from = '" + friend + "') or "
                    + "(user_to = '" + friend + "' and user_from = '" + myself + "') "
                    + "order by creation_time asc;";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String to = rs.getString("user_to");
                String from = rs.getString("user_from");
                String message = rs.getString("message");
                long time = rs.getLong("creation_time");
                boolean isRead = rs.getBoolean("is_readed");
                list.add(new ChatMessage(to, from, message, isRead, time));
            }

        } catch (Exception ex) {
        } finally {
            con.close();
        }
        return list;
    }

}
