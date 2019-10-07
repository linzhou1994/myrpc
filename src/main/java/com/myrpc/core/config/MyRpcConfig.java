package com.myrpc.core.config;

import com.myrpc.core.client.config.ClientProxyConfig;
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
    /**
     * 包路径分隔符
     */
    private static final String PACKAGE_PATH_SPLIT = ";";

    private Properties properties;
    /**
     * zk地址
     */
    private String zkAddress;
    /**
     * 需要扫描的包路径，多个包路径以";"隔开
     * 扫描包路径下的所有java文件，找出需要注册rpc服务的类
     */
    private String[] scanfPackagePaths;
    /**
     * 本机服务器属性
     */
    private ServerInfo serverInfo;
    /**
     * 客户端代理配置
     */
    private ClientProxyConfig clientProxyConfig;

    public MyRpcConfig(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("config file name error!");
        }

        init(fileName);

        initClientProxyConfig();

        initServerInfo();

    }

    private void init(String fileName) {
        properties = PropsUtil.loadProps(fileName);

        zkAddress = PropsUtil.getString(properties, "zkAddress");
        String scanfPackagePath = PropsUtil.getString(properties, "scanfPackagePath");
        if (StringUtils.isNotBlank(scanfPackagePath)) {
            this.scanfPackagePaths = scanfPackagePath.split(PACKAGE_PATH_SPLIT);
        }
    }

    private void initClientProxyConfig() {
        Long defaultOutTime = PropsUtil.getLong(properties, "defaultOutTime");
        int defaultRetryCount = PropsUtil.getInt(properties, "defaultRetryCount");
        clientProxyConfig = new ClientProxyConfig(defaultRetryCount, defaultOutTime);
    }

    private void initServerInfo() {
        String serverName = PropsUtil.getString(properties, "serverName");
        int myRpcPort = PropsUtil.getInt(properties, "myRpcPort");
        String address = SystemUtil.getIpAddress();
        serverInfo = new ServerInfo(serverName, address, myRpcPort);
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public ClientProxyConfig getClientProxyConfig() {
        return clientProxyConfig;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public String[] getScanfPackagePaths() {
        return scanfPackagePaths;
    }
}
