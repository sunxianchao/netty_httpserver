package cn.yunyoyo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.yunyoyo.util.RedisMonitorThread;
import cn.yunyoyo.util.SpringBeanUtil;

public class Bootstrap {
    
    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("classpath:application-context.xml");
        NettyHttpServer httpServer=SpringBeanUtil.getBean("nettyHttpServer", NettyHttpServer.class);
        RedisMonitorThread monitorThread=SpringBeanUtil.getBean("redisMonitorThread", RedisMonitorThread.class);
        monitorThread.start();
        httpServer.start();
        
    }

}
