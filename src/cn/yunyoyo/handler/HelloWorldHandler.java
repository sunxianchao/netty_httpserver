package cn.yunyoyo.handler;

import io.netty.handler.codec.http.HttpRequest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.yunyoyo.config.RedisConfig;
import cn.yunyoyo.service.RedisQueueService;
import cn.yunyoyo.service.RedisService;
import cn.yunyoyo.util.RequestUtil;

@Controller("helloWorldHandler")
@Scope("prototype")
public class HelloWorldHandler extends SimpleHttpResponseHandler implements RedisQueueService{

    @Autowired
    private RedisService redisService;
    
    @Override
    protected String httpResponseMessage(HttpRequest request) {
        System.out.println(redisService);
        StringBuilder sb=new StringBuilder();
        String uri=request.getUri();
        String queueName=getQueueNameFromUri(uri);
        String[] restUri=RequestUtil.getParameterFromUri(uri);
        if(uri.startsWith("/put/")){
            String msg=RequestUtil.getString(request, "msg");
            putMsgToQueue(queueName, msg);
            return "ok";
        }else if(uri.startsWith("/get/")){
            List<String> list=getMsgFromQueue(queueName);
            StringBuilder msg=new StringBuilder();
            for(String str:list){
                msg.append(str);
                msg.append("<br/>");
            }
            return msg.toString();
        }else if(uri.startsWith("/count/")){
            return String.valueOf(msgQueueSize(queueName));
        }else{
            for(int i=0;i<restUri.length;i++){
                sb.append(restUri[i]);
                sb.append("</br>");
            }
            String name=RequestUtil.getString(request, "name");
            String sex=RequestUtil.getString(request, "sex");
            sb.append("name="+name+"<br/>");
            sb.append("sex="+sex+"<br/>");
            
            return "hello world\t"+ new Date()+"<br/>"+sb;
        }
        
    }

    public void putMsgToQueue(String queue, String msg){
        try {
            redisService.rpush(RedisConfig.QUEUE_KEY_PREFIX+queue, msg);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getMsgFromQueue(String queue){
        try {
            String key=RedisConfig.QUEUE_KEY_PREFIX+queue;
            return redisService.lrange(key, 0, -1);
        } catch(Exception e) {
           e.printStackTrace();
        }
        return null;
    }
    
    public long msgQueueSize(String queue){
        try {
            String key=RedisConfig.QUEUE_KEY_PREFIX+queue;
            return redisService.llen(key);
        } catch(Exception e) {
           e.printStackTrace();
        }
        return 0;
    }
    
}
