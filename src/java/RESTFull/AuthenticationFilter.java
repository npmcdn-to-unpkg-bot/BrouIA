package RESTFull;

import database.DBConnection;
import java.io.IOException;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Authorize
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //Get the auth header
        String authorizationHeader
                = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        if (!isValidToken(token)) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return getUserName(token);
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });

    }

    private boolean isValidToken(String token) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select count(*) as count from users "
                    + "where hashed_pass = '" + token + "';";
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
    
    private String getUserName(String token) {
        DBConnection con = new DBConnection();

        try {
            con.open();
            Statement st = con.getConection().createStatement();
            String query = "select user_name count from users "
                    + "where hashed_pass = '" + token + "';";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                return rs.getString("user_name");
            }
        } catch (Exception ex) {
        } finally {
            con.close();
        }

        return "";
    }

}
