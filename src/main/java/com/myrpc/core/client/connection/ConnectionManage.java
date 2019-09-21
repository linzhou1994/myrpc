package com.myrpc.core.client.connection;


import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.common.bo.ServerInfo;
import com.myrpc.core.server.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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
 * 创建时间: 2019/9/22 0:06
 * 作者: linzhou
 * 描述: 客户端与服务端的连接管理类
 */
public class ConnectionManage {

    private static final Logger log = Logger.getLogger(ConnectionManage.class);

    private static final ConnectionManage CONNECTION_MANAGE = new ConnectionManage();

    private Map<String, Connection> address2Connection = new ConcurrentHashMap<>();

    private ConnectionManage() {
    }

    /**
     * 通过服务器ip和端口获取连接
     *
     * @param serverInfo 服务器信息
     * @return 一个可用的服务器连接
     */
    public static Connection getConnection(ServerInfo serverInfo) {
        return CONNECTION_MANAGE.getConnection0(serverInfo.getAddress(), serverInfo.getPort());
    }

    /**
     * 通过服务器ip和端口删除客户端连接
     *
     * @param serverInfo 服务器信息
     */
    public static void closeConnection(ServerInfo serverInfo) {
        closeConnection(serverInfo.getAddress(), serverInfo.getPort());
    }

    public static void closeConnection(String address, int port) {
        String key = getConnectionKey(address, port);
        CONNECTION_MANAGE.closeConnection(key);
    }

    /**
     * 通过服务器ip和端口获取连接
     *
     * @param address 服务器ip
     * @param port    端口
     * @return 可用的客户端连接
     */
    public Connection getConnection0(String address, int port) {
        if (StringUtils.isBlank(address) || port <= 0) return null;
        String key = getConnectionKey(address, port);
        //通过key从缓存中获取连接
        Connection connection = address2Connection.get(key);
        log.info("================getConnection0 key:" + key + " connection:" + connection);
        //如果没有拿到连接或者连接不可用则创建一个新的连接
        if (connection == null || !connection.isUsable()) {
            synchronized (key.intern()) {
                if (connection == null || !connection.isUsable()) {
                    connection = ClientConnection.createServerConnection(address, port);
                    if (connection.isUsable()) {
                        address2Connection.put(key, connection);
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
     * @param address 服务器ip地址
     * @param port    端口
     * @return key
     */
    private static String getConnectionKey(String address, int port) {
        return address + "." + port;
    }


    public static void main(String[] args) throws InterruptedException {
        sendMsg("127.0.0.1", 8888);
        sendMsg("127.0.0.1", 8888);
        sendMsg("127.0.0.1", 8880);


    }

    private static void sendMsg(String address, int port) throws InterruptedException {
        ServerInfo serverInfo = new ServerInfo(address, port);
        Connection connection = ConnectionManage.getConnection(serverInfo);
        log.info("==========================getConnection");
        if (connection != null) {
            Thread.sleep(3000);
            ClientRequest request = new ClientRequest();
            request.setClassNames(new String[]{"className"}).setMethodName("method");
            ServerResponse response = connection.sendMsg(request);
            log.info("==========================response1:" + response);
            request = new ClientRequest();
            request.setClassNames(new String[]{"className"}).setMethodName("method");
            response = connection.sendMsg(request);
            log.info("==========================response2:" + response);

        } else {
            log.info("==========================connection is null");
        }
    }


}
