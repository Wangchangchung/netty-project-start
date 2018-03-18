package com.wcc.netprotocol.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;


public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private WebSocketServerHandshaker serverHandler;

    public void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        //传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(channelHandlerContext, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(channelHandlerContext, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext context, FullHttpRequest request) {
        if (!request.getDecoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(context, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        //构造握手响应返回, 本机测试
        WebSocketServerHandshakerFactory webSocketFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
        serverHandler = webSocketFactory.newHandshaker(request);
        if (serverHandler == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
        }else {
            serverHandler.handshake(context.channel(), request);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext context, WebSocketFrame frame){
        //判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame){
            serverHandler.close(context.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame){
            context.channel().write( new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof  TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        //返回应答消息
        String request = ((TextWebSocketFrame) frame).text();

        //
        context.channel().write(new TextWebSocketFrame(request + " , 欢迎使用Netty WebSocket服务, 现在时刻: " + new Date().toString()));
    }



    private static void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest request, FullHttpResponse response) {
        //返回应答给客户端
        if (response.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            setContentLength(response, request.content().readableBytes());
        }
        //如果是非keep-Alive, 关闭连接
        ChannelFuture future = context.channel().writeAndFlush(response);
        if (!isKeepAlive(request) || response.getStatus().code() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
