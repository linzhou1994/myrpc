package com.myrpc.core.server.handler;

import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.server.ServerResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcServerHandler extends SimpleChannelInboundHandler<ClientRequest> {




    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientRequest request) throws Exception {
        System.out.println("接收客户端的消息："+request.toString());
        ServerResponse response = new ServerResponse(request.getUuid());
        //todo
        channelHandlerContext.channel().write(response);
        channelHandlerContext.flush();
    }
}
