package com.myrpc.core.client.connection;

import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.client.handler.ClientConnectionHandler;
import com.myrpc.core.common.handler.core.DefaultDecoder;
import com.myrpc.core.common.handler.core.ClientRequestEncoder;
import com.myrpc.core.netty.NettyClient;
import com.myrpc.core.server.ServerResponse;
import io.netty.channel.ChannelHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClientConnection implements Connection {
    private static final Logger log = Logger.getLogger(ClientConnection.class);

    private NettyClient client;

    private ClientConnectionHandler connectionHandler;

    public static ClientConnection createServerConnection(String address, int port) {
        try {
            return new ClientConnection(address, port);
        } catch (InterruptedException e) {
            return null;
        }
    }


    private ClientConnection(String address, int port) throws InterruptedException {
        createConnection(address, port);
    }

    private void createConnection(String address, int port) throws InterruptedException {
        log.info("==============createConnection==================");
        client = new NettyClient(address, port);
        List<ChannelHandler> channelHandlerList = getServerChannelHandlerList();
        client.setChildHandlerList(channelHandlerList);
        client.startClient();
    }

    private List<ChannelHandler> getServerChannelHandlerList() {
        List<ChannelHandler> handlerList = new ArrayList<>();
        //设置默认编码类解码类
        handlerList.add(new DefaultDecoder());
        //设置client请求server的请求类的编码类
        handlerList.add(new ClientRequestEncoder());
        connectionHandler = new ClientConnectionHandler();
        handlerList.add(connectionHandler);
        return handlerList;
    }

    /**
     * 向服务端发送消息
     *
     * @param request
     * @return
     */
    @Override
    public ServerResponse sendMsg(ClientRequest request) {
        try {
            return connectionHandler.sendMsg(request);
        } catch (Throwable e) {
            e.printStackTrace();
            return throwThrowable(e, request);
        }

    }

    /**
     * 客户端与服务端通信发生异常
     *
     * @param e
     * @param request
     * @return
     */
    private ServerResponse throwThrowable(Throwable e, ClientRequest request) {
        ServerResponse response = new ServerResponse(request.getUuid());
        response.setException(e);
        return response;
    }

    /**
     * 当前连接是否活跃
     *
     * @return
     */
    public boolean isUsable() {
        return client.isUsable();
    }
}
