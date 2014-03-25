package cn.yunyoyo.handler;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;


public class NotFoundHandler extends SimpleHttpResponseHandler {

    @Override
    protected String responseMessage(HttpRequest request) {
        logger.error("resource {} not found.", request.getUri());
        return "";
    }
    
    @Override
    protected HttpResponseStatus responseStatus() {
        return HttpResponseStatus.NOT_FOUND;
    }

}
