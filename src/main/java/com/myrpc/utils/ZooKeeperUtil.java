package com.myrpc.utils;

import org.apache.zookeeper.*;

import java.io.IOException;

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
 * @创建时间: 2019/9/24 20:50
 * @author: linzhou
 * @描述: ZooKeeperUtil
 */
public class ZooKeeperUtil {

    private static void create(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        int index = 1;
        while ((index = path.indexOf("/", index)) > 0) {
            String needCreatePath = path.substring(0,index);
            if (!exists(zk,needCreatePath)){
                zk.create(needCreatePath,"hallo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        }
    }

    private static boolean exists(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        return zk.exists("/idea", false) != null;
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
        create(zk,"/myrpc/server");
        Thread.sleep(6000);
    }
}
