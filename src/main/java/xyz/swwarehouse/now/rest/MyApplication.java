package xyz.swwarehouse.now.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by WES on 2017-05-01.
 */
@ApplicationPath("/")
public class MyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resource
        classes.add(TimeResource.class);
        return classes;
    }

}
