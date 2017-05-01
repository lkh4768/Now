package xyz.swwarehouse.now.netty;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.service.ServiceFinder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import xyz.swwarehouse.now.jersey.JerseyConfiguration;
import xyz.swwarehouse.now.netty.handler.NettyHandlerContainer;

/**
 * Created by WES on 2017-04-27.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private NettyHandlerContainer jerseyHandler;

    public ServerInitializer() {
        this.jerseyHandler = getJerseyHandler();
    }

    private NettyHandlerContainer getJerseyHandler() {
        ResourceConfig rcf = new JerseyConfiguration();
        return ContainerFactory.createContainer(NettyHandlerContainer.class, rcf);
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        /* custom handler */
        p.addLast(jerseyHandler);
    }
}
