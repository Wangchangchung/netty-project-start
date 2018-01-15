package com.wcc.netty.camonio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *伪异步IO的弊端
 * w伪异步IO 首先需要了解的是 当Socket的输入流进行读取操作的时候面，它会一直阻塞下去,
 * 直到发生如下的三种事情:
 * 1. 有数据可读
 * 2. 可用数据已经读取完毕
 * 3. 发生空指针或者I/O异常
 * 这意味者当对方发送请求或者应答消息比较缓慢，或者网络传输较慢的时候，读取输入流一方的通信线程将被长时间阻塞，
 * 如果对方要60s才能够将数据发送完成，读取一方的I/O线程也将会被同步阻塞60s, 在此期间，其他接入消息只能在消息队列中排队
 * 伪异步I/O实际上仅仅是堆之前I/O线程模型的一个简单优化, 它无法从根本上解决同步I/O导致的通信线程阻塞的问题
 * 
 */
public class TimeServer {

    public static void main(String[] args){
        int port = 8080;
        if (args!=null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //采用默认值
            }
        }
        ServerSocket  serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The time Server is start in port : " + port );
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutePool = new TimeServerHandlerExecutePool(50, 10000);
            while (true){
                socket = serverSocket.accept();
                singleExecutePool.execute(new TimeServerHandler(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                System.out.println("The time server close");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
        }
    }
}
