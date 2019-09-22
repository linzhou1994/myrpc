package com.myrpc.core.client.proxy;

import com.myrpc.core.consumer.MyRpcConsumer;
import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.client.config.ClientProxyConfig;
import com.myrpc.core.client.connection.Connection;
import com.myrpc.core.client.connection.ConnectionManage;
import com.myrpc.core.common.bo.ServerInfo;
import com.myrpc.core.exception.ServerException;
import com.myrpc.core.server.ServerResponse;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


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
 * 描述: 使用jdk动态代理的客户端代理类
 */

public class JavaClientProxy implements InvocationHandler {

    private static final Logger log = Logger.getLogger(JavaClientProxy.class);

    private ClientProxyConfig config;

    private Class[] clazzs;

    private String[] classNames;

    private MyRpcConsumer strategy;

    public JavaClientProxy(Class[] clazzs, MyRpcConsumer strategy) {
        this.strategy = strategy;
        this.clazzs = clazzs;
        classNames = new String[this.clazzs.length];
        for (int i = 0; i < 0; i++) {
            classNames[i] = this.clazzs[i].getName();
        }

    }

    public JavaClientProxy(ClientProxyConfig config, Class[] clazzs, MyRpcConsumer strategy) {
        this(clazzs, strategy);
        this.config = config;
    }

    private Object newProxyInstance() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), clazzs, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ClientRequest request = createClientRequest(config, classNames, method, args);
        if (strategy == null) {
            throw new IllegalArgumentException("MyRpcConsumer cannot is null!");
        }
        ServerInfo serverInfo = strategy.getServerInfo(request);
        if (serverInfo == null) {
            throw new ServerException("No find usable server！ClientRequest：" + request);
        }
        Connection connection = ConnectionManage.getConnection(serverInfo);
        if (connection == null || connection.isUsable()) {
            throw new ServerException("Unable to connect to server！ClientRequest：" + request);
        }
        ServerResponse response = connection.sendMsg(request);
        if (response == null) {
            log.info("Request time out！ClientRequest：" + request);
        }
        //如果服务端发送异常，则向外抛出异常
        if (response.isException()) {
            throw response.getException();
        }

        return response.getRlt();
    }

    private ClientRequest createClientRequest(ClientProxyConfig config, String[] classNames, Method method, Object[] args) {
        ClientRequest request = new ClientRequest();
        request.setClassNames(classNames)
                .setMethodName(method.getName())
                .setParams(args);
        if (config != null) {
            request.setRetryCount(config.getRetryCount())
                    .setTimeOut(config.getTimeOut());
        }
        return request;
    }
}
