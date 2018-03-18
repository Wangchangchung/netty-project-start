package com.wcc.netprotocol.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private WebSocketServerHandler serverHandler;

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        //传统的HTTP接入
        if (msg instanceof FullHttpRequest){

        }
    }

    private void handleHttpRequest(ChannelHandlerContext context, FullHttpRequest request){
        if (!request)
    }
}
