package com.myrpc.core.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.util.*;

public class NettyClient {
    private static final Logger log = Logger.getLogger(NettyClient.class);
    /**
     * 服务端ip地址
     */
    private String ipAddress;
    /**
     * 服务端启动端口
     */
    private int port;
    /**
     * 状态
     */
    private Status status;

    private EventLoopGroup eventLoopGroup;

    private List<ChannelHandler> childHandlerList;

    public NettyClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        status = Status.NEW;
    }

    public void addChannelHandler(ChannelHandler channelHandler) {
        if (childHandlerList == null) {
            childHandlerList = new ArrayList<>();
        }
        childHandlerList.add(channelHandler);
    }

    public void setChildHandlerList(List<ChannelHandler> childHandlerList) {
        this.childHandlerList = childHandlerList;
    }

    public void startClient() {
        status = Status.STARTING;
        new Thread(() -> {
            try {
                startClient0();
            } catch (Throwable e) {
                e.printStackTrace();
                status = Status.EXCEPTION;
            }
        }).start();
    }

    private void startClient0() throws InterruptedException {
        if (eventLoopGroup != null) return;

        log.info("==============startClient=============");

        eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {

                if (childHandlerList != null) {
                    log.info("============initChannel childHandlerList size:" + childHandlerList.size());
                    ChannelPipeline p = ch.pipeline();

                    for (int i = 0; i < childHandlerList.size(); i++) {
                        ChannelHandler handler = childHandlerList.get(i);
                        log.info("ChannelPipeline addLast:" + handler.getClass().getName());
                        p.addLast(handler);
                    }
                }
            }
        });

        ChannelFuture channelFuture = bootstrap.connect(ipAddress, port).sync();
        status = Status.START;
        channelFuture.channel().closeFuture().sync();
        status = Status.DEAD;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * 判断当前客户端是否可用
     * @return
     */
    public boolean isUsable() {
        return status == Status.NEW || status == Status.STARTING || status == Status.START;
    }
}
