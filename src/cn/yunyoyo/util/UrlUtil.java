/**
 * 
 */
package cn.yunyoyo.util;

import java.util.Map;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import cn.yunyoyo.config.URLConfig;

/**
 * @author xianchao.sun@downjoy.com
 * @date 2012-9-3
 */
public class UrlUtil {

    private static PathMatcher pathMatcher=new AntPathMatcher();

    /**
     * 判断所请求的URL 是否匹配所指定url pattern 列表。
     * @param requestUrl 请求的URL
     * @param urlResources URL Pattern 列表
     * @return
     */
    public static String getRequestUrlHandlerID(final String requestUrl, final Map<String, String> routers) {
        boolean isMatch=false;
        String tmpUrl=requestUrl;
        if(requestUrl.indexOf("?")>0){
            tmpUrl=requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        for (Map.Entry<String, String> m : routers.entrySet()) {
            isMatch=pathMatcher.match(m.getKey(), tmpUrl);
            if(isMatch){
                return m.getValue();
            }
        }
        return null;
    }
    
    public static boolean isSignUrl(final String requestUrl){
        boolean isMatch=false;
        String tmpUrl=requestUrl;
        if(requestUrl.indexOf("?")>0){
            tmpUrl=requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        isMatch=pathMatcher.match("/common/**", tmpUrl);
        return !isMatch;
    }
    
    public static void main(String[] args) {
        String u="/common/put/app";
        System.out.println(getRequestUrlHandlerID(u, URLConfig.getRoutes()));
    }

}
