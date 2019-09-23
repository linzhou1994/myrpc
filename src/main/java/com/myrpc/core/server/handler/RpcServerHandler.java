package com.myrpc.core.server.handler;

import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.common.bo.MethodHandler;
import com.myrpc.core.exception.ServerException;
import com.myrpc.core.server.ServerResponse;
import com.myrpc.core.server.container.ServiceContainerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;


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
 * @描述: RpcServerHandler
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<ClientRequest> {
    private static final Logger log = Logger.getLogger(RpcServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientRequest request) {
        log.info("接收客户端的消息：" + request.toString());
        ServerResponse response = new ServerResponse(request.getUuid());
        MethodHandler methodHander = ServiceContainerManager.CONTAINER.getMethodHander(request.getClassName(),
                request.getMethodName(),
                request.getParameterClassNames());
        if (methodHander != null) {
            try {
                Object rlt = methodHander.invoke(request.getParams());
                response.setRlt(rlt);
            } catch (Throwable e) {
                e.printStackTrace();
                response.setException(e);
            }
        } else {
            Throwable e = new ServerException("No find service!Please register first!");
            e.printStackTrace();
            response.setException(e);
        }

        channelHandlerContext.channel().write(response);
        channelHandlerContext.flush();
    }
}
