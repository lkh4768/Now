package xyz.swwarehouse.now.netty.handler;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.WebApplication;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.AsciiString;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by WES on 2017-05-01.
 */
@ChannelHandler.Sharable
public class NettyHandlerContainer extends ChannelHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(NettyHandlerContainer.class);

    public static final String PROPERTY_BASE_URI = "com.sun.jersey.server.impl.container.netty.baseUri";

    private static final AsciiString CONNECTION = new AsciiString("Connection");
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");

    private WebApplication application;
    private String baseUri;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public NettyHandlerContainer(WebApplication application, ResourceConfig resourceConfig) {
        this.application = application;
        this.baseUri = (String) resourceConfig.getProperty(PROPERTY_BASE_URI);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {

            HttpRequest req = (HttpRequest) msg;

            logger.debug(req.toString());

            if (HttpHeaderUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            boolean keepAlive = HttpHeaderUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
            String base = getBaseUri(req);
            final URI baseUri = new URI(base);
            final URI requestUri = new URI(base.substring(0, base.length() - 1) + req.uri());

            //if (!keepAlive) {
                final ContainerRequest cRequest = new ContainerRequest(application, req.method().toString(), baseUri, requestUri, getHeaders(req), null);
                application.handleRequest(cRequest, new JerseyContainerWriter(ctx.channel()));
                /*
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
            */

        }
    }

    private String getBaseUri(HttpRequest request) {
        if (baseUri != null) {
            return baseUri;
        }

        return "http://" + request.headers().get(HttpHeaderNames.HOST) + "/";
    }

    private InBoundHeaders getHeaders(HttpRequest request) {
        InBoundHeaders headers = new InBoundHeaders();

        for (Map.Entry<CharSequence, CharSequence> entry : request.headers().entries()) {
            headers.putSingle(entry.getKey().toString(), entry.getValue().toString());
        }

        return headers;
    }
}
