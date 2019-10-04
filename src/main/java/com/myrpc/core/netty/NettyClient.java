package com.myrpc.core.netty;

import com.myrpc.core.client.connection.ConnectionManage;
import com.myrpc.core.common.bo.ServerInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
 *
 * @创建时间: 2019/9/22 0:06
 * @author: linzhou
 * @描述: NettyClient
 */
public class NettyClient {
    private static final Logger log = Logger.getLogger(NettyClient.class);
    /**
     * 服务端信息
     */
    private ServerInfo serverInfo;

    /**
     * 状态
     */
    private Status status;

    private EventLoopGroup eventLoopGroup;

    private List<ChannelHandler> childHandlerList;



    public NettyClient(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        status = Status.NEW;
    }

    public void setChildHandlerList(List<ChannelHandler> childHandlerList) {
        this.childHandlerList = childHandlerList;
    }

    public void startClient() {
        status = Status.STARTING;
            try {
                startClient0();
            } catch (Throwable e) {
                e.printStackTrace();
                status = Status.EXCEPTION;
            } finally {
                ConnectionManage.closeConnection(serverInfo);
            }
    }

    private void startClient0() throws InterruptedException {
        if (eventLoopGroup != null) {
            return;
        }

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
                    ChannelPipeline p = ch.pipeline();

                    for (int i = 0; i < childHandlerList.size(); i++) {
                        ChannelHandler handler = childHandlerList.get(i);
                        p.addLast(handler);
                    }
                }
            }
        });

        ChannelFuture channelFuture = bootstrap.connect(serverInfo.getAddress(), serverInfo.getPort()).sync();
        status = Status.RUNNING;
        channelFuture.channel().closeFuture().sync();
        status = Status.DEAD;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * 判断当前客户端是否可用
     *
     * @return
     */
    public boolean isUsable() {
        return status == Status.NEW || status == Status.STARTING || status == Status.RUNNING;
    }
}
