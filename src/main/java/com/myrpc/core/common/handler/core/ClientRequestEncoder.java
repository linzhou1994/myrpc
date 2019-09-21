package com.myrpc.core.common.handler.core;


import com.myrpc.core.client.ClientRequest;
import com.myrpc.utils.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @作者 bg389315
 * @时间 2019/9/18
 * @描述 ClientRequest的编码类
 */
public class ClientRequestEncoder extends MessageToByteEncoder<ClientRequest> {
    private static final Logger log = Logger.getLogger(ClientRequestEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ClientRequest clientRequest, ByteBuf byteBuf) {
        //将对象转换为byte
        byte[] body = convertToBytes(clientRequest);
        //读取消息的长度
        int dataLength = body.length;
        //先将消息长度写入，也就是消息头
        byteBuf.writeInt(dataLength);
        //将消息写入
        byteBuf.writeBytes(body);
    }

    private byte[] convertToBytes(ClientRequest requestParam) {
        try {
            return Serializer.serialize(requestParam);
        } catch (IOException e) {
            return null;
        }
    }
}
