package com.wcc.netty.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by charse on 17-6-19.
 */
public class TimeServer {

    private static Logger logger = LoggerFactory.getLogger(TimeServer.class);

    public  static  void main(String[] args){
        int port = 8080;
        if (args !=null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
            }
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            logger.info("The Server is start in port:{}", port);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                try {
                    logger.info("The Server is close！");
                    serverSocket.close();
                    serverSocket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
