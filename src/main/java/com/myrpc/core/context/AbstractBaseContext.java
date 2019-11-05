package com.myrpc.core.context;

import com.myrpc.core.client.proxy.MyRpcClientProxy;
import com.myrpc.core.config.MyRpcConfig;
import com.myrpc.core.client.consumer.MyRpcConsumer;
import com.myrpc.core.server.provider.MyRpcProvider;
import com.myrpc.core.server.RpcServer;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * @创建时间: 2019/10/6 20:08
 * @author: linzhou
 * @描述: AbstractBaseContext
 */
public abstract class AbstractBaseContext implements RpcContext {

    private static final Logger log = Logger.getLogger(AbstractBaseContext.class);

    /**
     * 配置文件路径
     */
    protected String configFilePath;

    /**
     * myrpc配置信息
     */
    protected MyRpcConfig rpcConfig;

    /**
     * 注册中心消费者
     */
    protected MyRpcConsumer rpcConsumer;

    /**
     * 注册中心服务提供者
     */
    protected MyRpcProvider rpcProvider;

    /**
     * 动态代理工作类
     */
    protected MyRpcClientProxy proxy;

    /**
     * 服务端
     */
    protected RpcServer server;

    protected Map<Class<?>, Object> clazz2ClientProxyObj;

    public AbstractBaseContext() {
        clazz2ClientProxyObj = new ConcurrentHashMap<>();
    }

    /**
     * 获取需要注册Rpc服务的的对象集合
     *
     * @return 需要注册Rpc服务的的对象集合
     */
    protected abstract List<Object> getNeedRegisteredObjs();

    public void init() {
        List<Object> needRegisteredObjs = getNeedRegisteredObjs();
        if (needRegisteredObjs != null && !needRegisteredObjs.isEmpty()) {
            needRegisteredObjs.forEach(obj -> {
                try {
                    registered(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("registered obj error,obj toString" + obj.toString());
                }
            });
        }

        server = new RpcServer(rpcConfig.getServerInfo().getPort());
        server.startServer();
    }

    @Override
    public void registered(Object object) throws Exception {
        rpcProvider.registered(object);
    }

    @Override
    public <T> T getProxyObject(Class<?> clazz) {

        T rlt = (T) clazz2ClientProxyObj.get(clazz);
        if (rlt == null) {
            rlt = proxy.newProxyInstance(clazz, rpcConfig.getClientProxyConfig(), rpcConsumer);
            clazz2ClientProxyObj.put(clazz, rlt);
        }

        return rlt;
    }
}
