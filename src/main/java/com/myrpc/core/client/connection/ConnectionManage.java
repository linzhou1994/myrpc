package com.myrpc.core.client.connection;


import com.myrpc.core.common.bo.ServerInfo;
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

    private Map<String, Connection> address2Connection = new ConcurrentHashMap<>();

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
                if (connection == null || !connection.isUsable()) {
                    connection = ClientConnection.createClientConnection(serverInfo);
                    if (connection.isUsable()) {
                        address2Connection.put(key, connection);
                        connetionExecutor.execute(connection);
                    } else {
                        closeConnection(key);
                    }
                } else {
                    connection = address2Connection.get(key);
                }
            }

        }
        //如果最终拿到的依旧是一个不可用的连接，则返回null
        return connection.isUsable() ? connection : null;
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
        return serverInfo.getAddress() + "." + serverInfo.getPort();
    }


}
