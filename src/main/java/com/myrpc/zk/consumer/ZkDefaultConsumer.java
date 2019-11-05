package com.myrpc.zk.consumer;

import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.common.bo.ServerInfo;
import com.myrpc.core.config.MyRpcConfig;
import com.myrpc.core.client.consumer.MyRpcConsumer;
import com.myrpc.utils.ZooKeeperUtil;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

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
 * @创建时间: 2019/10/6 14:05
 * @author: linzhou
 * @描述: ZkDefaultConsumer
 */
public class ZkDefaultConsumer implements MyRpcConsumer {

    private static final Logger log = Logger.getLogger(ZkDefaultConsumer.class);

    private ZooKeeper zooKeeper;

    private MyRpcConfig myRpcConfig;

    private int pointer = 0;

    public ZkDefaultConsumer(ZooKeeper zooKeeper, MyRpcConfig myRpcConfig) {
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
    public ServerInfo getServerInfo(ClientRequest request) {
        String clazzName = request.getClassName();
        List<ServerInfo> data = ZooKeeperUtil.getData(zooKeeper, clazzName);
        if (!data.isEmpty()) {
            pointer %= data.size();
            return data.get(pointer++);
        }
        return null;
    }
}
