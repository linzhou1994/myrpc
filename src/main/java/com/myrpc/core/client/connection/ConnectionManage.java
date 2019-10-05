package com.myrpc.core.client.connection;


import com.myrpc.core.common.bo.ServerInfo;
import com.myrpc.core.netty.Status;
import com.myrpc.core.netty.listnner.ClientStatusChangeListnner;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * @描述: 客户端与服务端的连接管理类
 */
public class ConnectionManage {

    private static final Logger log = Logger.getLogger(ConnectionManage.class);

    private static final ConnectionManage CONNECTION_MANAGE = new ConnectionManage();

    /**
     * 客户端与服务端连接发生变化时的监听
     */
    private ClientStatusChangeListnner statusChangeListnner = (serverInfo, newStatu) -> {
        log.info("statusChangeListnner serverInfo:" + serverInfo.toString() + " newStatu:" + newStatu);
        //在创建新连接时主线程会等待客户端与服务端连接完成
        //如果连接已经完成或者发生异常或者连接关闭了，则唤醒主线程
        if (newStatu == Status.RUNNING || newStatu == Status.EXCEPTION || newStatu == Status.CLOSE) {
            String key = serverInfo.toString().intern();
            synchronized (key) {
                key.notifyAll();
            }
        }
    };

    /**
     * 客户端与服务端连接的缓存
     */
    private Map<String, Connection> address2Connection = new ConcurrentHashMap<>();
    /**
     * 控制客户端与服务端连接的线程池
     */
    private ExecutorService connetionExecutor = Executors.newCachedThreadPool();

    private ConnectionManage() {
    }

    /**
     * 通过服务器ip和端口获取连接
     *
     * @param serverInfo 服务器信息
     * @return 一个可用的服务器连接
     */
    public static Connection getConnection(@NotNull ServerInfo serverInfo) {
        return CONNECTION_MANAGE.getConnection0(serverInfo);
    }

    /**
     * 通过服务器ip和端口删除客户端连接
     *
     * @param serverInfo 服务器信息
     */
    public static void closeConnection(@NotNull ServerInfo serverInfo) {
        String key = getConnectionKey(serverInfo);
        log.info("close connection key:" + key);
        CONNECTION_MANAGE.closeConnection(key);
    }

    /**
     * 通过服务器ip和端口获取连接
     *
     * @param serverInfo 服务器信息
     * @return 可用的客户端连接
     */
    public Connection getConnection0(ServerInfo serverInfo) {
        if (StringUtils.isBlank(serverInfo.getAddress()) || serverInfo.getPort() <= 0) {
            return null;
        }
        String key = getConnectionKey(serverInfo);
        //通过key从缓存中获取连接
        Connection connection = address2Connection.get(key);
        //如果没有拿到连接或者连接不可用则创建一个新的连接
        if (connection == null || !connection.isUsable()) {

            synchronized (key.intern()) {
                //从缓存里面重新拿一次连接
                connection = address2Connection.get(key);
                //如果还是没拿到或者不可用，则创建一个新的连接
                if (connection == null || !connection.isUsable()) {
                    //创建连接（但并不与服务端建立通讯）
                    connection = ClientConnection.createClientConnection(serverInfo, statusChangeListnner);
                    //使用线程池启动连接
                    connetionExecutor.execute(connection);
                    //注册连接
                    registeConnection(key, connection);
                    Status status;
                    //如果当前连接处于创建状态或者启动中状态则等待
                    if ((status = connection.getStatus()) == Status.NEW || status == Status.STARTING) {
                        try {
                            key.intern().wait(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }


            }

        }

        //如果最终拿到的依旧是一个不可用的连接，则返回null
        return connection.isUsable() ? connection : null;
    }

    private void registeConnection(String key, Connection newConnection) {
        //获取旧的连接
        Connection oldConnection = address2Connection.get(key);
        //存在旧的连接并且就的连接与新连接不是同一个对象，则关闭旧的连接，再注册新的连接
        if (oldConnection != null && oldConnection != newConnection) {
            closeConnection(key);
        }
        address2Connection.put(key, newConnection);
    }

    /**
     * 通过客户端连接的缓存key删除客户端连接
     *
     * @param key
     */
    public void closeConnection(String key) {
        Connection connection = address2Connection.remove(key);
        //关闭移除的连接
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 通过服务器ip地址和端口获取客户端连接的缓存key
     *
     * @param serverInfo 服务器信息
     * @return key
     */
    private static String getConnectionKey(ServerInfo serverInfo) {
        return serverInfo.toString();
    }

    public static void main(String[] args) {
        ServerInfo serverInfo = new ServerInfo("127.0.0.1", 8888);
        System.out.println("connectiong is null :" + (null == getConnection(serverInfo)));
        closeConnection(serverInfo);
    }

}
