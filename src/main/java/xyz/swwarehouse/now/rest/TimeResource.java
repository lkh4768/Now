package xyz.swwarehouse.now.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by WES on 2017-05-01.
 */
@Path("/now")
public class TimeResource {
    private static final Logger logger = LogManager.getLogger(TimeResource.class);
    @GET
    @Produces(MediaType.TEXT_HTML)
    public DummyResponse getClichedMessage() {
        logger.debug("entered");
        DummyResponse response = new DummyResponse();
        logger.debug("1");
        response.setMessage("Hello World");
        logger.debug("2");
        return response;
    }
}
