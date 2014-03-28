package cn.yunyoyo.config;

import java.util.LinkedHashMap;

/**
 * 初始化url资源，如果有更新手动添加
 * url 匹配支持ant风格的url匹配规则
 * @author Sunxc
 */
public class URLConfig {

    static final String commonurl="/common/**/hello";// 以common开头的url不需要做验证
    
    static final String hello="/**/hello*";
    
    // 应用收集兼容老的url 使用根路径处理
    static final String apps="/**/app";
    
    final static LinkedHashMap<String, String> routes=new LinkedHashMap<String, String>();

    static {
        routes.put(hello, "helloWorldHandler");
        routes.put(apps, "appCollectionHandler");
    }

    public static LinkedHashMap<String, String> getRoutes() {
        return routes;
    }
}
