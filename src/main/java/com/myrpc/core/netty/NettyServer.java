package com.myrpc.core.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import java.util.*;


public class NettyServer {

    private static final Logger log = Logger.getLogger(NettyServer.class);

    //服务端启动端口
    private int port;
    /**
     * 状态
     */
    private Status status;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private List<Class<? extends ChannelHandler>> childHandlerList;

    public NettyServer(int port) {
        this.port = port;
        status = Status.NEW;
    }

    public void setChildHandlerList(List<Class<? extends ChannelHandler>> childHandlerList) {
        this.childHandlerList = childHandlerList;
    }

    public void start() throws InterruptedException {
        if (bossGroup != null && workerGroup != null) return;
        status = Status.STARTING;
        log.info("==============startServer=============");
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {

                            if (childHandlerList != null) {
                                ChannelPipeline p = ch.pipeline();

                                childHandlerList.forEach(clzz -> {
                                    try {
                                        log.info("ChannelPipeline addLast Class:" + clzz.getName());
                                        p.addLast((ChannelHandler) clzz.newInstance());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                            }
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            status = Status.START;
            f.channel().closeFuture().sync();
            status = Status.DEAD;
        } catch (Throwable e) {
            e.printStackTrace();
            status = Status.EXCEPTION;
            throw e;
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public Status getStatus() {
        return status;
    }
}
