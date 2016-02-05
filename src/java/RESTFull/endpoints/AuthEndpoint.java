package RESTFull.endpoints;

import database.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthEndpoint {

    @POST
    @Path("/token")
    public Response authenticateUser(@HeaderParam("username") String username,
            @HeaderParam("password") String password) {

        if (authenticate(username, password)) {
            String token = issueToken(username);
            return Response.ok(token).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/register")
    public Response registerUser(@HeaderParam("username") String username,
            @HeaderParam("password") String password) {
        if (insertUser(username, password)) {
            String token = issueToken(username);
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/changepass")
    public Response changeUserPass(@HeaderParam("username") String username,
            @HeaderParam("password") String password) {
        if (updateUser(username, password)) {
            String token = issueToken(username);
            return Response.ok(token).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private boolean insertUser(String username, String password) {
        DBConnection con = new DBConnection();
        long time = System.currentTimeMillis() / 1000L;
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "insert into users values("
                    + "'" + username + "', "
                    + time + ", "
                    + "sha2(concat('" + time + "', '" + password + "'), 512));";
            st.execute(query);
            return true;
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return false;
    }

    private boolean updateUser(String username, String password) {
        DBConnection con = new DBConnection();
        long time = System.currentTimeMillis() / 1000L;
        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "update users set "
                    + "creation_time = " + time + ", "
                    + "hashed_pass = sha2(concat('" + time + "', '" + password + "'), 512) "
                    + "where user_name = '" + username + "';";
            st.execute(query);
            return true;
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return false;
    }

    private boolean authenticate(String username, String password) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select count(*) as count from users "
                    + "where user_name = '" + username + "' "
                    + "and hashed_pass = "
                    + "sha2(concat(creation_time, '" + password + "'), 512) "
                    + "collate 'latin1_spanish_ci';";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                int total = rs.getInt("count");
                return 1 == total;
            }
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return false;
    }

    private String issueToken(String username) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select hashed_pass as token "
                    + "from users "
                    + "where user_name = '" + username + "';";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                return rs.getString("token");
            }
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return "";
    }
}
