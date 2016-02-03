package database;

import Models.ChatMessage;
import java.io.BufferedWriter;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("/chat")
public class APIChat {

    private final static List<ChatMessage> CHAT
            = Collections.synchronizedList(new ArrayList<ChatMessage>());

    @GET
    @Path("/read/{myself}")
    public Response getMyself(@PathParam("myself") String name) {

        StreamingOutput stream = (OutputStream output) -> {
            Writer writer = new BufferedWriter(new OutputStreamWriter(output));

            long currentTime = System.currentTimeMillis() / 1000L;
            for (ChatMessage msg : CHAT) {
                if (!msg.isRead() && msg.getTo().equals(name) && msg.getTime() >= currentTime) {
                    writer.write(msg.toString() + "\n");
                    msg.ReadMessage();
                }
            }
            writer.flush();
        };

        return Response.ok(stream).build();
    }

//    @GET
//    @Path("/past/{myself}")
//    public Response getPast(@PathParam("myself") String name) {
//        
//    }
    
    @POST
    @Path("/send")
    public Response send(@HeaderParam("from") String from,
            @HeaderParam("to") String to, @HeaderParam("msg") String msg) {
        final ChatMessage cm = new ChatMessage(to, from, msg);
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
                    + cm.getTo() + ", "
                    + cm.getFrom() + ", " 
                    + cm.getMessage() + ", "
                    + cm.getTime() + ");";
            st.execute(query);
            
            
        } catch (Exception ex) {
        } finally {
            con.close();
        }
    }
    
    private List<ChatMessage> getMessagesFromUserDB(String user) {
        ArrayList<ChatMessage> list = new ArrayList();
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select to, from, time, message from messages "
                    + "where to = " + user + ";";
            ResultSet rs = st.executeQuery(query);
            
            
        } catch (Exception ex) {
        } finally {
            con.close();
        }
        return list;
    }
}
