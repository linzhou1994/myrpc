package com.myrpc.core.server;


import com.myrpc.core.common.handler.core.DefaultDecoder;
import com.myrpc.core.common.handler.core.ServerResponseEncoder;
import com.myrpc.core.netty.NettyServer;
import com.myrpc.core.server.handler.RpcServerHandler;
import io.netty.channel.ChannelHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RpcServer {
    private static final Logger log = Logger.getLogger(RpcServer.class);

    private int port;

    private NettyServer server;

    public RpcServer(int port) {
        this.port = port;
    }

    public void startServer() {
        log.info("=========服务端开始启动 端口：" + port + "==============");
        server = new NettyServer(port);
        List<Class<?extends ChannelHandler>> serverChannelHandlerList = getServerChannelHandlerList();
        try {
            server.setChildHandlerList(serverChannelHandlerList);
            server.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("=========服务端启动异常================");
        }
        log.info("=========服务端已关闭==============");
    }

    private List<Class<?extends ChannelHandler>> getServerChannelHandlerList() {
        List<Class<?extends ChannelHandler>> handlerClassList = new ArrayList<>();
        //设置默认编码类解码类
        handlerClassList.add(DefaultDecoder.class);
        //设置client请求server的请求类的编码类
        handlerClassList.add(ServerResponseEncoder.class);

        handlerClassList.add(RpcServerHandler.class);
        return handlerClassList;
    }

    public static void main(String[] args) {
        RpcServer server = new RpcServer(8888);
        server.startServer();
    }

}
