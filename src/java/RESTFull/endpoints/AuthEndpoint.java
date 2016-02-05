package RESTFull.endpoints;

import RESTFull.Authorize;
import database.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/auth")
public class AuthEndpoint {

    @GET
    @Authorize
    @Path("/info")
    public Response userInfo(@Context SecurityContext securityContext) {
        StringBuilder json = new StringBuilder();
        String name = securityContext.getUserPrincipal().getName();
        json.append("{\"name\":\"")
                .append(name)
                .append("\"}");
        
        return Response.ok(json.toString()).build();
    }
    
    @POST
    @Path("/signin")
    public Response signinUser(@FormParam("username") String username,
            @FormParam("password") String password, @Context UriInfo uriInfo) {

        if (authenticate(username, password)) {
            String token = issueToken(username);
            String baseUrl = uriInfo.getBaseUri().toString();
            baseUrl = baseUrl.substring(0, baseUrl.length() - 4);
            return Response
                    .status(Response.Status.SEE_OTHER)
                    .header("Location", baseUrl + "?auth-token=" + token)
                    .build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/register")
    public Response registerUser(@FormParam("username") String username,
            @FormParam("password") String password, @Context UriInfo uriInfo) {
        if (insertUser(username, password)) {
            String token = issueToken(username);
            String baseUrl = uriInfo.getBaseUri().toString();
            baseUrl = baseUrl.substring(0, baseUrl.length() - 4); // menos "/api"
            return Response
                    .status(Response.Status.SEE_OTHER)
                    .header("Location", baseUrl + "?auth-token=" + token)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Authorize
    @Path("/changepass")
    public Response changeUserPass(@FormParam("username") String username,
            @FormParam("password") String password, @Context UriInfo uriInfo) {
        if (updateUser(username, password)) {
            String token = issueToken(username);
            String baseUrl = uriInfo.getBaseUri().toString();
            baseUrl = baseUrl.substring(0, baseUrl.length() - 4); // menos "/api"
            return Response
                    .status(Response.Status.SEE_OTHER)
                    .header("Location", baseUrl + "?auth-token=" + token)
                    .build();
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
