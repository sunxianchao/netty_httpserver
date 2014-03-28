package cn.yunyoyo.handler;

import org.springframework.stereotype.Service;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

@Service("notFoundHandler")
public class NotFoundHandler extends SimpleHttpResponseHandler {

    @Override
    protected String httpResponseMessage(HttpRequest request) {
        logger.error("resource {} not found.", request.getUri());
        return "";
    }
    
    @Override
    protected HttpResponseStatus responseStatus() {
        return HttpResponseStatus.NOT_FOUND;
    }

}
