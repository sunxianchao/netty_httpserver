package cn.yunyoyo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import cn.yunyoyo.handler.router.RouterHandler;


public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("timeout", new ReadTimeoutHandler(30));
        pipeline.addLast("handler", new RouterHandler());
    }
    
}
