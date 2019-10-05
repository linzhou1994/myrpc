package com.myrpc.utils;

import com.myrpc.core.common.bo.ServerInfo;
import org.apache.zookeeper.*;

import java.io.IOException;
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
 * @创建时间: 2019/9/24 20:50
 * @author: linzhou
 * @描述: ZooKeeperUtil
 */
public class ZooKeeperUtil {

    private static final String ROOT_PATH = "/myrpc";

    private static final String PATH_SPLIT = "/";

    public static boolean create(ZooKeeper zk, String classPath, ServerInfo serverInfo) {
        try {
            String path = ROOT_PATH;
            //创建根节点
            if (createPersistentNode(zk, path)) {
                return false;
            }

            if (!PATH_SPLIT.equals(classPath.substring(0, 1))) {
                path = path + PATH_SPLIT + classPath;
            } else {
                path = path + classPath;

            }
            //创建二级节点，节点路径为类路径
            if (createPersistentNode(zk, path)) {
                return false;
            }

            path = path + PATH_SPLIT + serverInfo.toString();
            byte[] data = Serializer.serialize(serverInfo);
            if (exists(zk, path)) {
                zk.setData(path, data, 0);
            } else {
                try {
                    zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (exists(zk, path)) {
                        zk.setData(path, data, 0);
                    } else {
                        return false;
                    }
                }
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T> List<T> getData(ZooKeeper zk, String classPath) {
        List<T> rlt = new ArrayList<>();
        List<String> serverStrList = null;
        String path = ROOT_PATH + PATH_SPLIT + classPath;

        try {
            serverStrList = zk.getChildren(path, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serverStrList != null && !serverStrList.isEmpty()) {

            serverStrList.forEach(serverStr -> {
                try {
                    byte[] data = zk.getData(path + PATH_SPLIT + serverStr, false, null);
                    T t = (T) Serializer.deserialize(data);
                    rlt.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return rlt;
    }

    private static boolean createPersistentNode(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        if (!exists(zk, path)) {
            try {
                zk.create(path, path.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (Exception e) {
                e.printStackTrace();
                if (!exists(zk, path)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean exists(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        return zk.exists(path, false) != null;
    }

    public static void main(String[] args) throws IOException {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
        ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
        ZooKeeper zk3 = new ZooKeeper("127.0.0.1:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });

        ServerInfo serverInfo1 = new ServerInfo("8888", "127.0.0.1", 8888);
        ServerInfo serverInfo2 = new ServerInfo("8080", "127.0.0.1", 8080);
        String classPath = ZooKeeperUtil.class.getName();
        System.out.println("class path :" + classPath);
        System.out.println("zk create serverinfo1 result:" + create(zk, classPath, serverInfo1));
        System.out.println("zk2 create serverinfo2 result:" + create(zk2, classPath, serverInfo2));

        List<ServerInfo> serverInfoList = getData(zk3, classPath);

        System.out.println("serverInfoList toString:" + serverInfoList.toString());


    }
}
