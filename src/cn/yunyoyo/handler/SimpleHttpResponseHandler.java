package cn.yunyoyo.handler;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class SimpleHttpResponseHandler extends SimpleChannelInboundHandler<Object> {

    static final InternalLogger logger=InternalLoggerFactory.getInstance(SimpleHttpResponseHandler.class);

    private static final String DEFAULT_CONTENT_TYPE="text/html; charset=UTF-8";

    private static final HttpResponseStatus OK=HttpResponseStatus.OK;
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest request=(HttpRequest)msg; // 将其强制转化为httprequest
            boolean keepAlive=isKeepAlive(request); // 判断当前的连接时否是keepalive的

            ByteBuf b=ctx.alloc().buffer();

            String resMsg=responseMessage(request);
            b.writeBytes(resMsg.getBytes("UTF-8"));
            HttpResponseStatus status=responseStatus();

            FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1, status, b);
            response.headers().set(CONTENT_TYPE, getContentType());
            response.headers().set(CONTENT_LENGTH, b.readableBytes());
            if(!keepAlive) {
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
        }
    }

    /**
     * 子类实现返回客户端信息的方法
     * @param request
     * @return
     */
    protected abstract String responseMessage(HttpRequest request);

    /**
     * 子类覆盖并实现自己的返回状态码
     * @return
     */
    protected HttpResponseStatus responseStatus() {
        return OK;
    }

    /**
     * 子类覆盖实现自己的返回类型方法
     * @return
     */
    protected String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    /**
     * restfull 风格url从中提前参数
     * @param startPrefix
     * @param uri
     * @return
     */
    protected String[] getParameterFromUri(String uri) {
        uri=uri.replaceAll("\\?.*", "").replaceFirst("/", "");
        return StringUtil.split(uri.trim(), '/');
    }
    
    protected static Object getParameter(HttpRequest request, String paramsName) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> params=queryStringDecoder.parameters();
        if(!params.isEmpty() && params.containsKey(paramsName)){
            if(!params.get(paramsName).isEmpty()){
                return params.get(paramsName).get(0);
            }
        }
        return null;
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().remove(this);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    
}
