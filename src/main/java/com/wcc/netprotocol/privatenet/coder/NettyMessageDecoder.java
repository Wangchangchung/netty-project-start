package com.wcc.netprotocol.privatenet.coder;

import com.wcc.netprotocol.privatenet.common.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import java.util.List;
import java.util.Map;

public class NettyMessageDecoder extends MessageToMessageEncoder<NettyMessage>{

    MarshallingEncoder marshallingEncoder;

    public NettyMessageDecoder() {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          NettyMessage nettyMessage, List<Object> list) throws Exception {
        if (nettyMessage == null || nettyMessage.getHeader() ==null){
            throw new Exception("The encode message is null");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(nettyMessage.getHeader().getCrcCode());
        sendBuf.writeInt(nettyMessage.getHeader().getLength());
        sendBuf.writeLong(nettyMessage.getHeader().getSessionID());
        sendBuf.writeByte(nettyMessage.getHeader().getType());
        sendBuf.writeByte(nettyMessage.getHeader().getPriority());
        sendBuf.writeByte(nettyMessage.getHeader().getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param: nettyMessage.getHeader().getAttachment().entrySet()){
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.
        }
    }


}
