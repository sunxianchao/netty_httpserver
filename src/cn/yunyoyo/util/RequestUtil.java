package cn.yunyoyo.util;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author xianchao.sun@downjoy.com
 */
public class RequestUtil {

    /**
     * 判断是否为AJAX请求
     * @param request
     * @return
     */
    public static boolean isAjax(HttpRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.headers().get("X-Requested-With"));
    }

    /**
     * 取参数的Long 值
     * @param request
     * @param paraName
     * @return
     */
    public static Long getLong(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStr=paramsMap.get(paraName);
        if(tempStr == null || tempStr.isEmpty()) {
            return null;
        }
        String val=tempStr.get(0);
        if(val == null || val.length() == 0){
            return null;
        }
        try{
            return Long.parseLong(val);
        }catch(Exception ex){   
        }
        return null;
    }

    /**
     * 取参数的Long 值数组
     * @param request
     * @param paraName
     * @return
     */
    public static List<Long> getLongs(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStrArray=paramsMap.get(paraName);

        List<Long> valueArray=new ArrayList<Long>();
        if(null != tempStrArray && !tempStrArray.isEmpty()) {
            for(String tempStr: tempStrArray) {
                if(null != tempStr && tempStr.length() !=0) {
                    try{
                        valueArray.add(Long.parseLong(tempStr.trim()));
                    }catch(Exception ex){
                    }
                }
            }
        }
        return valueArray;
    }

    /**
     * 取参数值
     * @param request
     * @param paraName
     * @return
     */
    public static String getString(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tmp=paramsMap.get(paraName);
        if(null == tmp || tmp.isEmpty()) {
            return null;
        }
        String val = tmp.get(0);
        if(null == val || val.length()==0) {
            return "";
        }
        return val.trim();
    }

    /**
     * 取参数的Integer值
     * @param request
     * @param paraName
     * @return
     */
    public static Integer getInteger(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStr=paramsMap.get(paraName);
        if(tempStr == null || tempStr.isEmpty()) {
            return null;
        }
        String val=tempStr.get(0);
        if(val == null || val.length() == 0){
            return null;
        }
        try{
            return Integer.parseInt(val);
        }catch(Exception ex){   
        }
        return null;
    }

    /**
     * 取参数的Double值
     * @param request
     * @param paraName
     * @return
     */
    public static Double getDouble(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);        
        List<String> tempStr=paramsMap.get(paraName);
        if(tempStr == null || tempStr.isEmpty()) {
            return null;
        }
        String val=tempStr.get(0);
        if(val == null || val.length() == 0){
            return null;
        }
        try{
            return Double.parseDouble(val);
        }catch(Exception ex){   
        }
        return null;
    }

    /**
     * 取参数的Float值
     * @param request
     * @param paraName
     * @return
     */
    public static Float getFloat(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);        
        List<String> tempStr=paramsMap.get(paraName);
        if(tempStr == null || tempStr.isEmpty()) {
            return null;
        }
        String val=tempStr.get(0);
        if(val == null || val.length() == 0){
            return null;
        }
        try{
            return Float.parseFloat(val);
        }catch(Exception ex){   
        }
        return null;
    }

    /**
     * 取参数的Date值
     * @param request
     * @param paraName
     * @param format
     * @return
     */
    public static Date getDate(HttpRequest request, String paraName, String format) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStr=paramsMap.get(paraName);
        if(tempStr == null || tempStr.isEmpty()) {
            return null;
        }
        String val= tempStr.get(0);
        try {
            return new SimpleDateFormat(format).parse(val);
        } catch(Exception e) {
        }
        return null;
    }



    /**
     * 取参数的Boolean值
     * @param request
     * @param paraName
     * @return
     */
    public static Boolean getBoolean(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStr=paramsMap.get(paraName);
        if(tempStr == null || tempStr.isEmpty()) {
            return null;
        }
        String val=tempStr.get(0);
        if("true".equalsIgnoreCase(val) || "1".equals(val)) {
            return true;
        }
        return false;
    }

    /**
     * 取参数的Integer值列表
     * @param request
     * @param paraName
     * @return
     */
    public static List<Integer> getIntegers(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStrArray=paramsMap.get(paraName);

        List<Integer> valueArray=new ArrayList<Integer>();
        if(null != tempStrArray && !tempStrArray.isEmpty()) {
            for(String tempStr: tempStrArray) {
                if(null != tempStr && tempStr.length() !=0) {
                    try{
                        valueArray.add(Integer.parseInt(tempStr.trim()));
                    }catch(Exception ex){
                    }
                }
            }
        }
        return valueArray;
    }

    /**
     * 取参数的Double值列表
     * @param request
     * @param paraName
     * @return
     */
    public static List<Double> getDoubles(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStrArray=paramsMap.get(paraName);

        List<Double> valueArray=new ArrayList<Double>();
        if(null != tempStrArray && !tempStrArray.isEmpty()) {
            for(String tempStr: tempStrArray) {
                if(null != tempStr && tempStr.length() !=0) {
                    try{
                        valueArray.add(Double.parseDouble(tempStr.trim()));
                    }catch(Exception ex){
                    }
                }
            }
        }
        return valueArray;
    }

    /**
     * 取参数的Float值列表
     * @param request
     * @param paraName
     * @return
     */
    public static List<Float> getFloats(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStrArray=paramsMap.get(paraName);

        List<Float> valueArray=new ArrayList<Float>();
        if(null != tempStrArray && !tempStrArray.isEmpty()) {
            for(String tempStr: tempStrArray) {
                if(null != tempStr && tempStr.length() !=0) {
                    try{
                        valueArray.add(Float.parseFloat(tempStr.trim()));
                    }catch(Exception ex){
                    }
                }
            }
        }
        return valueArray;
    }

    /**
     * 取参数的值
     * @param request
     * @param paraName
     * @return
     */
    public static List<String> getStringArray(HttpRequest request, String paraName) {
        Map<String, List<String>> paramsMap=getRequestMap(request);
        List<String> tempStrArray=paramsMap.get(paraName);
        return tempStrArray;
    }


    /**
     * 获取用户的真实IP
     * @param request
     * @return
     *//*
    public static String getUserIpAddr(HttpServletRequest request) {
        String unknown="unknown";
        String ip = request.getHeader("X-Real-IP");
        if(StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        
        if(StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(StringUtils.isBlank(ip) || unknown.equalsIgnoreCase(ip)) {           
            ip = request.getRemoteAddr();
        }
        if(null != ip && ip.length() != 0) {
            String tmpIps[]=ip.split(",");
            for(String tmpIp: tmpIps) {
                if(null == tmpIp) {
                    continue;
                }
                tmpIp=tmpIp.trim();
                // 过滤本机IP和内网IP，内网IP地址:10.x.x.x;192.168.x.x;172.16.x.x至172.31.x.x。由于172.16.x.x至172.31.x.x比较少见故不做过滤。
                if(tmpIp.length() != 0 && !unknown.equalsIgnoreCase(tmpIp) && tmpIp.indexOf("127.0.0.1") == -1
                    && !tmpIp.startsWith("192.168.") && !tmpIp.startsWith("10.")) {
                    return tmpIp;
                }
            }
        }
        return request.getRemoteAddr();
    }

    *//**
     * 获取用户的代理IP
     * @param request
     * @return
     *//*
    public static String getSubIpAddr(HttpRequest request) {
        String unknown="unknown";
        HttpHeaders headers=request.headers();
        String ip=headers.get("x-forwarded-for");
        if(ip != null && ip.length() > 0 && !unknown.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip=headers.get("Proxy-Client-IP");
        if(ip != null && ip.length() > 0 && !unknown.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip=headers.get("WL-Proxy-Client-IP");
        if(ip != null && ip.length() > 0 && !unknown.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }*/

    @SuppressWarnings("unchecked")
    public static void printAllHeaders(HttpRequest request) {
        HttpHeaders headers=request.headers();
        Iterator<String> it=headers.names().iterator();
        System.out.println("<------------------print header begin----------------------->");
        String name, val;
        while(it.hasNext()) {
            name=it.next();
            val=headers.get(name);
            System.out.println(name +"="+ val);
        }
        System.out.println("<------------------print header end------------------------->");
    }
    
    private static Map<String, List<String>> getRequestMap(HttpRequest request){
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> paramsMap=queryStringDecoder.parameters();
        return paramsMap;
    }
}
