package cn.yunyoyo.util;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import cn.yunyoyo.service.RedisService;

@Service("redisMonitorThread")
public class RedisMonitorThread extends Thread {

    @Autowired
    private RedisService redisService;
    
    static final InternalLogger logger=InternalLoggerFactory.getInstance(RedisMonitorThread.class);

    private boolean ALIVE=true;

    private boolean EXCEPTION_FALG=false;
    
    public void run() {
        int sleepTime=30000;
        int baseSleepTime=1000;
        logger.info("redis 服务启动...");
        while(true) {
            try {
                // 30秒执行监听
                int n=sleepTime / baseSleepTime;
                for(int i=0; i < n; i++) {
                    if(EXCEPTION_FALG) {// 检查到异常，立即进行检测处理
                        break;
                    }
                    Thread.sleep(baseSleepTime);
                }
                // 连续做3次连接获取
                int errorTimes=0;
                for(int i=0; i < 3; i++) {
                    try {
                        Jedis jedis=redisService.getJedis();
                        if(jedis == null) {
                            errorTimes++;
                            continue;
                        }
                        redisService.releaseJedisInstance(jedis);
                        break;
                    } catch(Exception e) {
                        errorTimes++;
                        continue;
                    }
                }
                if(errorTimes == 3) {// 3次全部出错，表示服务器出现问题
                    ALIVE=false;
                    logger.error("redis[" + redisService.getServerName() + "] 服务器连接不上！ ！ ！");
                    // 修改休眠时间为5秒，尽快恢复服务
                    sleepTime=5000;
                } else {
                    if(ALIVE == false) {
                        ALIVE=true;
                        // 修改休眠时间为30秒，尽快恢复服务
                        sleepTime=30000;
                        logger.info("redis[" + redisService.getServerName() + "] 服务器恢复正常！ ！ ！");
                    }
                    EXCEPTION_FALG=false;
                    Jedis jedis=redisService.getJedis();
                    logger.info("redis[" + redisService.getServerName() + "] 当前记录数：" + jedis.dbSize());
                    redisService.releaseJedisInstance(jedis);
                }
            } catch(Exception e) {
            }
        }
    }
}
