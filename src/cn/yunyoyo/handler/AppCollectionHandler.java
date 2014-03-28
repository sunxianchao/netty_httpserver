package cn.yunyoyo.handler;

import io.netty.handler.codec.http.HttpRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.yunyoyo.service.RedisQueueService;
import cn.yunyoyo.service.RedisService;
import cn.yunyoyo.util.RequestUtil;

@Service("appCollectionHandler")
@Scope("prototype")
public class AppCollectionHandler extends SimpleHttpResponseHandler implements RedisQueueService{

    @Autowired
    private RedisService redisService;
    
    @Override
    public void putMsgToQueue(String queue, String msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getMsgFromQueue(String queue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long msgQueueSize(String queue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected String httpResponseMessage(HttpRequest request) {
        String action=RequestUtil.getString(request, "action");
        if(action != null && action.equals("put")){// 老的应用url 直接废弃
            return null;
        }
        String uri=request.getUri();
        String msg=RequestUtil.getString(request, "msg");
        String queueName=getQueueNameFromUri(uri);
        if(uri.startsWith("/put/")){
            putMsgToQueue(queueName, msg);
        }else if(uri.startsWith("/get/")){
            getMsgFromQueue(queueName);
        }else if(uri.startsWith("/count/")){
            msgQueueSize(queueName);
        }
        return null;
    }

}
