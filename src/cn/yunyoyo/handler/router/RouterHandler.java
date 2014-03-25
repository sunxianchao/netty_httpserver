package cn.yunyoyo.handler.router;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.yunyoyo.handler.NotFoundHandler;
import cn.yunyoyo.handler.SimpleHttpResponseHandler;
import cn.yunyoyo.service.RedisService;

/**
 * netty httpserver 路由的实现，所有请求达到这里根据uri进行转发到不同的handler进行处理
 * @author Sunxc
 */
@Sharable
public class RouterHandler extends SimpleChannelInboundHandler<Object> {

    static final InternalLogger logger=InternalLoggerFactory.getInstance(RouterHandler.class);

    private Map<String, Class<?>> routers=new LinkedHashMap<String, Class<?>>();

    private ChannelHandler defaultHandler;

    private boolean handleNotFound;
    
    private RedisService redisService;

    public RouterHandler(LinkedHashMap<String, Class<?>> routers, RedisService redisService)throws Exception {
        this.redisService=redisService;
        this.routers=routers;
        this.handleNotFound=true;
        this.defaultHandler=null;
    }
    
    public RouterHandler(LinkedHashMap<String, Class<?>> routers, boolean handleNotFound, ChannelHandler defaultHandler)
        throws Exception {
        this.handleNotFound=handleNotFound;
        this.defaultHandler=defaultHandler;
        this.routers=routers;
         
    }

    public RouterHandler(LinkedHashMap<String, Class<?>> routers, boolean handleNotFound) throws Exception {
        this.handleNotFound=handleNotFound;
        this.defaultHandler=null;
        this.routers=routers;
    }

    public RouterHandler(LinkedHashMap<String, Class<?>> routers) throws Exception {
        this.handleNotFound=true;
        this.defaultHandler=null;
        this.routers=routers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest request=(HttpRequest)msg;
            String uri=request.getUri();
            boolean matchFound=false;

            for (Map.Entry<String, Class<?>> m : routers.entrySet()) {
                if(uri.startsWith(m.getKey())){// 默认是匹配前缀
                    Class<?> handlerClass=routers.get(m.getKey());
                    addOrReplaceHandler(ctx.pipeline(), handlerClass, "route-generated");
                    matchFound=true;
                    break;
                }
            }
            // 如果匹配到了url处理的handler那么就把默认的handler从pipeline中移除
            if(matchFound && defaultHandler != null) {
                removeHandler(ctx.pipeline(), "default-handler");
            }

            // 没有匹配到url处理器并且默认的handler不为空，那么使用默认的处理器
            if(!matchFound && defaultHandler != null) {
                addOrReplaceHandler(ctx.pipeline(), defaultHandler, "default-handler");
            } else if(!matchFound && handleNotFound) {
                // 否则就是默认的handler（404）处理
                addOrReplaceHandler(ctx.pipeline(), new NotFoundHandler(), "404-handler");
            }
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private void addOrReplaceHandler(ChannelPipeline pipeline, ChannelHandler handler, String handleName) throws Exception {
        synchronized(pipeline) {
            System.out.println(this);
            System.out.println(pipeline);
            System.out.println(pipeline.names());
            if(pipeline.get(handleName) == null) {
                pipeline.addLast(handleName, handler);
            } else {
                pipeline.replace(handleName, handleName, handler);
            }
            System.out.println(pipeline);
        }
    }

    private void addOrReplaceHandler(ChannelPipeline pipeline, Class<?> classz, String handleName) throws ClassNotFoundException,
        InstantiationException, IllegalAccessException {
        synchronized(pipeline) {
            System.out.println(this);
            System.out.println(pipeline);
            System.out.println(pipeline.names());
            SimpleHttpResponseHandler handler=(SimpleHttpResponseHandler)Class.forName(classz.getName()).newInstance();
            if(pipeline.get(handleName) == null) {
                pipeline.addLast(handleName, handler);
            } else {
                pipeline.replace(handleName, handleName, handler);
            }
            System.out.println(pipeline);
        }
    }

    private void removeHandler(ChannelPipeline pipeline, String handleName) {
        synchronized(pipeline) {
            if(pipeline.get(handleName) != null) {
                System.out.println("removing handle: " + handleName);
                pipeline.remove(handleName);
            }
        }
    }
    
}
