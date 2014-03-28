package cn.yunyoyo.handler.router;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import cn.yunyoyo.Contents;
import cn.yunyoyo.config.URLConfig;
import cn.yunyoyo.handler.NotFoundHandler;
import cn.yunyoyo.handler.SimpleHttpResponseHandler;
import cn.yunyoyo.util.RSACoder;
import cn.yunyoyo.util.RequestUtil;
import cn.yunyoyo.util.SpringBeanUtil;
import cn.yunyoyo.util.UrlUtil;

/**
 * netty httpserver 路由的实现，所有请求达到这里根据uri进行转发到不同的handler进行处理
 * @author Sunxc
 */
@Sharable
public class RouterHandler extends SimpleChannelInboundHandler<Object> {

    static final InternalLogger logger=InternalLoggerFactory.getInstance(RouterHandler.class);

    private ChannelHandler defaultHandler;

    private boolean handleNotFound;
    
    public RouterHandler(boolean handleNotFound, ChannelHandler defaultHandler)
        throws Exception {
        this.handleNotFound=handleNotFound;
        this.defaultHandler=defaultHandler;
         
    }

    public RouterHandler(boolean handleNotFound) throws Exception {
        this.handleNotFound=handleNotFound;
        this.defaultHandler=null;
    }
    
    public RouterHandler(){
        this.defaultHandler=null;
        this.handleNotFound=true;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest request=(HttpRequest)msg;
            String uri=request.getUri();
            boolean matchFound=false;

            String beanName=UrlUtil.getRequestUrlHandlerID(uri, URLConfig.getRoutes());
            if(null != beanName && beanName.length()>0){
                addOrReplaceHandler(ctx.pipeline(), beanName, "route-generated");
                matchFound=true;
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
            if(UrlUtil.isSignUrl(uri) && matchFound){// 验证url是否需要验证，common开头的rul会被拦截验证
                checkSignData(request);
            }
            ctx.fireChannelRead(msg);
        }
        
        if (msg instanceof LastHttpContent) {
            ctx.writeAndFlush("\n").addListener(ChannelFutureListener.CLOSE);
        }
        

    }

    private boolean checkSignData(HttpRequest request) throws Exception{
        String data=RequestUtil.getSignData(request);
        String sign=RequestUtil.getString(request, "sign");
        boolean isSafe=RSACoder.verify(data, Contents.PUBLICKEY, sign);
        if(!isSafe){
            throw new Exception("sign error");
        }
        return isSafe;
    }

    private void addOrReplaceHandler(ChannelPipeline pipeline, ChannelHandler handler, String handleName) throws Exception {
        synchronized(pipeline) {
            if(pipeline.get(handleName) == null) {
                pipeline.addLast(handleName, handler);
            } else {
                pipeline.replace(handleName, handleName, handler);
            }
        }
    }

    private void addOrReplaceHandler(ChannelPipeline pipeline, String beanName, String handleName) throws ClassNotFoundException,
        InstantiationException, IllegalAccessException {
        synchronized(pipeline) {
            SimpleHttpResponseHandler handler=(SimpleHttpResponseHandler)SpringBeanUtil.getBean(beanName);
            if(pipeline.get(handleName) == null) {
                pipeline.addLast(handleName, handler);
            } else {
                pipeline.replace(handleName, handleName, handler);
            }
        }
    }

    private void removeHandler(ChannelPipeline pipeline, String handleName) {
        synchronized(pipeline) {
            if(pipeline.get(handleName) != null) {
                pipeline.remove(handleName);
            }
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ByteBuf b=ctx.alloc().buffer();
        StringBuilder resMsg=new StringBuilder();
        resMsg.append("<h1>500 ERROR</h1><br/>http server response error");
        b.writeBytes(resMsg.toString().getBytes("UTF-8"));
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, b);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, b.readableBytes());        
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        ctx.close();
        logger.error("handlerError:", cause);
    }
}
