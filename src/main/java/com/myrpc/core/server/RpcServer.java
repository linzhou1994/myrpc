package com.myrpc.core.server;


import com.myrpc.core.common.handler.core.DefaultDecoder;
import com.myrpc.core.common.handler.core.ServerResponseEncoder;
import com.myrpc.core.netty.NettyServer;
import com.myrpc.core.server.handler.RpcServerHandler;
import io.netty.channel.ChannelHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
 * @描述: RpcServer
 */
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
        List<Class<? extends ChannelHandler>> serverChannelHandlerList = getServerChannelHandlerList();
        try {
            server.setChildHandlerList(serverChannelHandlerList);
            server.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("=========服务端启动异常================");
        }
        log.info("=========服务端已关闭==============");
    }

    private List<Class<? extends ChannelHandler>> getServerChannelHandlerList() {
        List<Class<? extends ChannelHandler>> handlerClassList = new ArrayList<>();
        //设置默认编码类解码类
        handlerClassList.add(DefaultDecoder.class);
        //设置client请求server的请求类的编码类
        handlerClassList.add(ServerResponseEncoder.class);

        handlerClassList.add(RpcServerHandler.class);
        return handlerClassList;
    }


}
