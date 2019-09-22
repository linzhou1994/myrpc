package com.myrpc.core.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //         佛祖保佑           永无BUG           永不修改           //
 * //          佛曰:                                                 //
 * //                 写字楼里写字间，写字间里程序员;                 //
 * //                 程序人员写程序，又拿程序换酒钱.                 //
 * //                 酒醒只在网上坐，酒醉还来网下眠;                 //
 * //                 酒醉酒醒日复日，网上网下年复年.                 //
 * //                 但愿老死电脑间，不愿鞠躬老板前;                 //
 * //                 奔驰宝马贵者趣，公交自行程序员.                 //
 * //                 别人笑我忒疯癫，我笑自己命太贱;                 //
 * //                 不见满街漂亮妹，哪个归得程序员?                 //
 * ////////////////////////////////////////////////////////////////////
 * 创建时间: 2019/9/22 0:06
 * 作者: linzhou
 * 描述: NettyServer
 */
public class NettyServer {

    private static final Logger log = Logger.getLogger(NettyServer.class);

    /**
     * 服务端启动端口
     */
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
        if (bossGroup != null && workerGroup != null) {
            return;
        }
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

                                childHandlerList.forEach(clazz -> {
                                    try {
                                        log.info("ChannelPipeline addLast Class:" + clazz.getName());
                                        p.addLast((ChannelHandler) clazz.newInstance());
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
