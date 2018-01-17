package com.wcc.netty.camonio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by charse on 17-6-19.
 */
public class TimeServerHandler implements Runnable {

    private Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    // 线程 run 方法
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // 得到输入流
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            // 得到输出流
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                //输出 请求的信息
                logger.info("The  time server receive order:{}", body);
                // 执行时间
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
                        : "DAD ORDER";
                logger.info(currentTime);
            }

        } catch (IOException e) {
            // 出现异常就直接关闭操作。
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                    this.socket = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }
}
