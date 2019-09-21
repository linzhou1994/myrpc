package com.myrpc.core.common.handler.core;


import com.myrpc.core.server.ServerResponse;
import com.myrpc.utils.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;

/**
 * @作者 bg389315
 * @时间 2019/9/18
 * @描述 ServerResponse的编码类
 */
public class ServerResponseEncoder extends MessageToByteEncoder<ServerResponse> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ServerResponse serverResponse, ByteBuf byteBuf) {
        byte[] body = convertToBytes(serverResponse);  //将对象转换为byte
        int dataLength = body.length;  //读取消息的长度
        byteBuf.writeInt(dataLength);  //先将消息长度写入，也就是消息头
        byteBuf.writeBytes(body);
    }

    private byte[] convertToBytes(ServerResponse resultParam) {
        try {
            return Serializer.serialize(resultParam);
        } catch (IOException e) {
            return null;
        }
    }
}
