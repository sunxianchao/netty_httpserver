package cn.yunyoyo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public static Object getBean(String id) {
        if(ctx == null) {
            throw new NullPointerException("ApplicationContext is null");
        }
        return ctx.getBean(id);
    }
    
    public static <T> T getBean(String name, Class<T> requiredType) {
        return ctx.getBean(name, requiredType);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx=applicationContext;
    }
}
