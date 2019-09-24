package com.myrpc.core.provider;

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

    private ZooKeeper zooKeeper;

    /**
     *
     * @param zooKeeper  zk连接
     * @param zkServerRootPath 当前服务在zk中的根路径
     */
    public ZkDefaultProvider(ZooKeeper zooKeeper,String zkServerRootPath) {
        if (zooKeeper == null){
            throw new IllegalArgumentException("zooKeeper cannot is null!");
        }
        this.zooKeeper = zooKeeper;
    }

    public void registered0(Object object) throws Exception {

    }
}
