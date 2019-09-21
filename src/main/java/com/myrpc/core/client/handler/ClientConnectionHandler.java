package com.myrpc.core.client.handler;


import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.netty.NettyClient;
import com.myrpc.core.server.ServerResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionHandler extends SimpleChannelInboundHandler<ServerResponse> {

    private final Map<String, ClientRequest> requestParamMap = new ConcurrentHashMap<>();
    private ChannelHandlerContext ctx;

    private final NettyClient client;

    /**
     * 是否需要关闭当前连接
     */
    private Boolean needClose;

    public ClientConnectionHandler(NettyClient client) {
        if (client == null) {
            throw new IllegalArgumentException("NettyClient cannot null,Please initialize nettyclient first!");
        }
        this.client = client;
        this.needClose = false;
    }

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
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        synchronized (needClose) {
            if (needClose) {
                ctx.close();
            } else {
                this.ctx = ctx;
            }
        }

    }

    /**
     * 向服务端发送请求
     *
     * @param clientRequest
     * @return
     * @throws Exception
     */
    public ServerResponse sendMsg(ClientRequest clientRequest) throws Exception {
        //校验请求参数合法性
        checkClientRequest(clientRequest);

        try {
            synchronized (clientRequest) {

                requestParamMap.put(clientRequest.getUuid(), clientRequest);

                int retryCount = clientRequest.getRetryCount();
                long timeOut = clientRequest.getTimeOut();
                //如果重试次数没有小于0并且未收到服务端的返回并且客户端连接依然可用，则重试向服务端发送消息
                while (retryCount-- < 0 && clientRequest.getResponse() == null && client.isUsable()) {

                    if (ctx != null) {
                        ctx.writeAndFlush(clientRequest);
                    }

                    //等待服务端响应
                    clientRequest.wait(timeOut);
                }

                return clientRequest.getResponse();
            }
        } finally {
            requestParamMap.remove(clientRequest.getUuid());
        }
    }

    /**
     * 校验请求参数合法性
     *
     * @param clientRequest
     */
    private void checkClientRequest(ClientRequest clientRequest) {
        StringBuilder sb = new StringBuilder();
        if (clientRequest.getRetryCount() < 0) {
            sb.append("retryCount must be greater than  or equal to 0;");
        }
        if (clientRequest.getTimeOut() <= 0) {
            sb.append("timeOut must be greater than 0;");
        }
        if (StringUtils.isBlank(clientRequest.getClassName())) {
            sb.append("className cannot be empty;");
        }
        if (StringUtils.isBlank(clientRequest.getMethod())) {
            sb.append("method cannot be empty;");
        }
        if (sb.length() > 0) {
            throw new IllegalArgumentException(sb.toString());
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

    /**
     * 关闭当前客户端与服务端的连接
     */
    public void close() {
        synchronized (needClose) {
            if (isActivity()) {
                ctx.close();
            } else {
                needClose = true;
            }
        }
    }
}
