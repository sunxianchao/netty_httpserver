package cn.yunyoyo;

import cn.yunyoyo.config.RedisConfig;
import cn.yunyoyo.util.PropertiesUtil;


public class Bootstrap {

    public static void main(String[] args) throws NumberFormatException, Exception {
        String port=PropertiesUtil.getValue("netty.port");
        RedisConfig.startReids();
        new NettyHttpServer(Integer.parseInt(port)).run();
    }

}
