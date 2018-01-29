package com.wcc.netty.aio;

/**
 * Created by charse on 17-6-23.
 */
public class TimeServer {

    public  static  void main(String[] args){
        int port = 8080;
        try {
            if (args != null && args.length > 0) {
                port = Integer.valueOf(args[0]);
            }
        }catch (NumberFormatException e){
            // 使用默认的 端口 8080
        }
        // AIO 时间服务器
        AsyncTimeServerHandler timeServerHandler = new
                AsyncTimeServerHandler(port);
        new Thread(timeServerHandler, "AIO-AsyncTimeServerHandler-001").start();
    }
}
