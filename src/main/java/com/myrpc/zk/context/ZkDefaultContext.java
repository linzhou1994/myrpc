package com.myrpc.zk.context;

import com.myrpc.core.annotation.MyRpcServer;
import com.myrpc.core.annotation.ServerInit;
import com.myrpc.core.client.proxy.DefaultJavaClientProxy;
import com.myrpc.core.config.MyRpcConfig;
import com.myrpc.core.context.AbstractBaseContext;
import com.myrpc.utils.ClassUtil;
import com.myrpc.utils.ReflectionUtil;
import com.myrpc.zk.consumer.ZkDefaultConsumer;
import com.myrpc.zk.provider.ZkDefaultProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.lang.reflect.Method;
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
 * @创建时间: 2019/10/7 22:17
 * @author: linzhou
 * @描述: ZkDefaultContext
 */
public class ZkDefaultContext extends AbstractBaseContext {

    private static final Logger log = Logger.getLogger(ZkDefaultContext.class);

    private ZooKeeper zk;

    private Watcher watcher;

    public ZkDefaultContext() throws IOException {
        this(null);
    }

    public ZkDefaultContext(String configFile) throws IOException {
        if (StringUtils.isBlank(configFile)){
            configFilePath = "myrpc.properties";
        }else {
            configFilePath = configFile;
        }
        rpcConfig = new MyRpcConfig(configFilePath);

        watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        };

        zk = new ZooKeeper(rpcConfig.getRegistrationCenterAddress(), 2000, watcher);

        rpcConsumer = new ZkDefaultConsumer(zk, rpcConfig);

        rpcProvider = new ZkDefaultProvider(zk, rpcConfig);

        proxy = new DefaultJavaClientProxy();

    }

    @Override
    protected List<Object> getNeedRegisteredObjs() {
        List<Object> rlt = new ArrayList<>();

        String[] scanfPackagePaths = rpcConfig.getScanfPackagePaths();
        if (scanfPackagePaths != null && scanfPackagePaths.length > 0) {

            for (String packagePath : scanfPackagePaths) {
                ClassUtil.getClassSet(packagePath).forEach(clazz -> {
                    if (clazz.isAnnotationPresent(MyRpcServer.class)) {
                        try {
                            //创建实例对象
                            Object obj = clazz.newInstance();
                            //执行初始化方法
                            invokeObjInitMethod(obj, clazz);
                            rlt.add(obj);

                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("newInstance error,class name :" + clazz.getName());
                        }
                    }
                });
            }
        }
        return rlt;
    }

    private void invokeObjInitMethod(Object obj, Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(ServerInit.class)) {
                    ReflectionUtil.invokeMethod(obj, method, null);
                }
            }
        }
    }
}
