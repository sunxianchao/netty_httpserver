package cn.yunyoyo.service;

import java.util.List;


public interface RedisQueueService {

    public void putMsgToQueue(String queue, String msg);
    
    public List<String> getMsgFromQueue(String queue);
}
