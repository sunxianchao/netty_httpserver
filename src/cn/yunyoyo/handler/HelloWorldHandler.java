package cn.yunyoyo.handler;

import io.netty.handler.codec.http.HttpRequest;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cn.yunyoyo.config.RedisConfig;
import cn.yunyoyo.service.RedisQueueService;
import cn.yunyoyo.service.RedisService;
import cn.yunyoyo.util.RequestUtil;


public class HelloWorldHandler extends SimpleHttpResponseHandler implements RedisQueueService{

    //后期通过spring ioc注入
    private RedisService redisService=new RedisService();
    
    @Override
    protected String responseMessage(HttpRequest request) {
        StringBuilder sb=new StringBuilder();
        String uri=request.getUri();
        String[] restUri=getParameterFromUri(uri);
        if(uri.startsWith("/put/")){
            String msg=RequestUtil.getString(request, "msg");
            String queueName=restUri[1];
            putMsgToQueue(queueName, msg);
            return "ok";
        }else if(uri.startsWith("/get/")){
            String queueName=restUri[1];
            List<String> list=getMsgFromQueue(queueName);
            StringBuilder msg=new StringBuilder();
            for(String str:list){
                msg.append(str);
            }
            return msg.toString();
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
        } catch(TimeoutException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getMsgFromQueue(String queue){
//        byte[] listKey=RedisService.serializable(RedisConfig.QUEUE_KEY_PREFIX+queue);
        try {
            return redisService.blpop(RedisConfig.TIMEOUT, RedisConfig.QUEUE_KEY_PREFIX+queue);
        } catch(Exception e) {
           e.printStackTrace();
        }
        return null;
    }
    
}
