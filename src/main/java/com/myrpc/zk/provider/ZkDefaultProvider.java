package com.myrpc.zk.provider;

import com.myrpc.core.config.MyRpcConfig;
import com.myrpc.core.server.provider.BaseMyRpcProvider;
import com.myrpc.utils.ReflectionUtil;
import com.myrpc.utils.ZooKeeperUtil;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

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
 * @创建时间: 2019/9/24 20:42
 * @author: linzhou
 * @描述: ZkDefaultProvider zk默认提供者
 */
public class ZkDefaultProvider extends BaseMyRpcProvider {

    private static final Logger log = Logger.getLogger(ZkDefaultProvider.class);

    private ZooKeeper zooKeeper;

    private MyRpcConfig myRpcConfig;

    /**
     * @param zooKeeper zk连接
     */
    public ZkDefaultProvider(ZooKeeper zooKeeper, MyRpcConfig myRpcConfig) {
        if (zooKeeper == null) {
            throw new IllegalArgumentException("zooKeeper cannot is null!");
        }
        if (myRpcConfig == null) {
            throw new IllegalArgumentException("myRpcConfig cannot is null!");
        }
        this.zooKeeper = zooKeeper;
        this.myRpcConfig = myRpcConfig;
    }

    @Override
    public void registered0(Object object) {
        //得到当前要注册的对象的类对象
        Class<?> clazz = object.getClass();
        //获取当前类对象实现的接口名称
        String[] clazzNames = ReflectionUtil.getInterfaceNames(clazz);
        //将每一个接口名称都注册到zk上
        for (String clazzName : clazzNames) {

            boolean registeResult = ZooKeeperUtil.create(zooKeeper, clazzName, myRpcConfig.getServerInfo());
            if (registeResult) {
                log.info("myrpc registered success, class name:" + clazzName);
            } else {
                log.error("myrpc registered error, class name:" + clazzName);
            }
        }

    }
}
