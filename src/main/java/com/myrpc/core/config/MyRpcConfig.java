package com.myrpc.core.config;

import com.myrpc.core.common.bo.ServerInfo;
import com.myrpc.utils.PropsUtil;
import com.myrpc.utils.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

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
 * @创建时间: 2019/9/24 20:21
 * @author: linzhou
 * @描述: MyRpcConfig 系统配置参数读取存储类
 */
public class MyRpcConfig {

    private Properties properties;
    /**
     * zk地址
     */
    private String zkAddress;
    /**
     * 本机服务器属性
     */
    private ServerInfo serverInfo;
    /**
     * 默认请求超时时间（可以不填，不填则为3000ms）
     */
    private Long defaultOutTime;
    /**
     * 默认请求重试次数（以不填，不填则不重试）
     */
    private Integer defaultRetryCount;

    public MyRpcConfig(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("config file name error!");
        }
        properties = PropsUtil.loadProps(fileName);

        zkAddress = PropsUtil.getString(properties, "zkAddress");
        defaultOutTime = PropsUtil.getLong(properties, "defaultOutTime");
        defaultRetryCount = PropsUtil.getInt(properties, "defaultRetryCount");


        String serverName = PropsUtil.getString(properties, "serverName");
        int myRpcPort = PropsUtil.getInt(properties, "myRpcPort");
        String address = SystemUtil.getIpAddress();

        serverInfo = new ServerInfo(serverName, address, myRpcPort);

    }

    public String getZkAddress() {
        return zkAddress;
    }

    public Long getDefaultOutTime() {
        return defaultOutTime;
    }

    public Integer getDefaultRetryCount() {
        return defaultRetryCount;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }
}
