package com.myrpc.core.common.bo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

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
 * @创建时间: 2019/9/22 0:21
 * @author: linzhou
 * @描述: 服务器信息存储类
 */
public class ServerInfo implements Serializable {
    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 服务器ip
     */
    private String address;
    /**
     * 服务器端口
     */
    private int port;

    public ServerInfo(String address, int port) {
        this.address = address;
        this.port = port;
    }


    public ServerInfo(String serverName, String address, int port) {
        this.address = address;
        this.port = port;
        this.serverName = serverName;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerInfo) {
            ServerInfo serverInfo = (ServerInfo) obj;
            return StringUtils.equals(serverName, serverInfo.getServerName())
                    && StringUtils.equals(address, serverInfo.getAddress())
                    && port == serverInfo.getPort();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int rlt = 17;
        rlt = 31 * rlt + (serverName == null ? 0 : serverName.hashCode());
        rlt = 31 * rlt + (address == null ? 0 : address.hashCode());
        rlt = 31 * rlt + port;
        return rlt;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}
