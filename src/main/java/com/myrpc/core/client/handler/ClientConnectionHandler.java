package com.myrpc.core.client.handler;


import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.server.ServerResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionHandler extends SimpleChannelInboundHandler<ServerResponse> {

    private Map<String, ClientRequest> requestParamMap = new ConcurrentHashMap<>();
    private ChannelHandlerContext ctx;

    /**
     * 本方法用于接收服务端发送过来的消息
     *
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerResponse response) throws Exception {
        System.out.println(response);
        String key = response.getUuid();
        ClientRequest clientRequest = getRequestParam(key);
        clientRequest.setResponse(response);
        synchronized (clientRequest) {
            clientRequest.notifyAll();
        }
    }

    /**
     * 本方法用于处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 本方法用于向服务端发送信息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public ServerResponse sendMsg(ClientRequest clientRequest) throws Exception {
        try {
            synchronized (clientRequest) {
                requestParamMap.put(clientRequest.getUuid(), clientRequest);

                ctx.writeAndFlush(clientRequest);
                //等待服务端响应
                clientRequest.wait(3000);

                return clientRequest.getResponse();
            }
        } finally {
            requestParamMap.remove(clientRequest.getUuid());
        }
    }

    public ClientRequest getRequestParam(String key) {
        return requestParamMap.get(key);
    }

    /**
     * 当前连接是否活跃
     *
     * @return
     */
    public boolean isActivity() {
        return ctx != null;
    }
}
