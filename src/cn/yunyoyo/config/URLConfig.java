package cn.yunyoyo.config;

import java.util.LinkedHashMap;

import cn.yunyoyo.handler.HelloWorldHandler;
import cn.yunyoyo.handler.NotFoundHandler;

/**
 * 初始化url资源，如果有更新手动添加
 * url 匹配支持ant风格的url匹配规则
 * @author Sunxc
 */
public class URLConfig {

    final String hello="/**/hello";
    
    final String favicon_ico="/favicon.ico";

    final LinkedHashMap<String, Class<?>> routes=new LinkedHashMap<String, Class<?>>();

    public URLConfig() {
        routes.put(hello, HelloWorldHandler.class);
        routes.put(favicon_ico, NotFoundHandler.class);
    }

    public LinkedHashMap<String, Class<?>> getRoutes() {
        return this.routes;
    }
}
