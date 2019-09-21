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
 * 描述: 客户端连接实现类
 */
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
        connectionHandler = new ClientConnectionHandler(this.client);
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

    @Override
    public void close() {
        connectionHandler.close();
    }
}
