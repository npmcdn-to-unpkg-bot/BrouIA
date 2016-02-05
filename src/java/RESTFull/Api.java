package RESTFull;

import RESTFull.endpoints.*;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class Api extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        
        //Filters
        resources.add(AuthenticationFilter.class);
        
        //Endpoints
        resources.add(DataEndpoint.class);
        resources.add(ChatEndpoint.class);
        resources.add(AuthEndpoint.class);
        
        return resources;
    }
}