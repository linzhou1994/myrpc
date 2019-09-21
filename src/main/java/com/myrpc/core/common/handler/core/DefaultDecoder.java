package com.myrpc.core.common.handler.core;

import com.myrpc.utils.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @作者 bg389315
 * @时间 2019/9/18
 * @描述 解码
 */
public class DefaultDecoder extends ByteToMessageDecoder {

    private static final Logger log = Logger.getLogger(DefaultDecoder.class);

    public static final int HEAD_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        //这个HEAD_LENGTH用于表示头长度的字节数。  由于上面我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
        if (byteBuf.readableBytes() < HEAD_LENGTH) {
            return;
        }
        //标记一下当前的readIndex的位置
        byteBuf.markReaderIndex();
        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        int dataLength = byteBuf.readInt();
        // 读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
        if (dataLength < 0) {
            context.close();
        }
        //读到的消息体长度如果小于传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        //根据长度创建字节数组接收
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);  //
        //将byte数据转化为需要的对象
        Object o = convertToObject(body);
        list.add(o);

    }

    /**
     * 将字节反序列化成对象
     *
     * @param body
     * @return
     */
    private Object convertToObject(byte[] body) {
        try {
            return Serializer.deserialize(body);
        } catch (Exception e) {
            return null;
        }
    }
}
