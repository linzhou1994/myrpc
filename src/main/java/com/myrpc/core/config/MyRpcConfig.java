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

    /**
     * 配置文件注册中心地址设置
     */
    private static final String REGISTRATION_CENTER_ADDRESS = "registrationCenterAddress";
    /**
     * MyRpc需要扫描的包路径设置
     */
    private static final String SCANF_PACKAGE_PATH = "scanfPackagePath";
    /**
     * 默认超时时间设置
     */
    private static final String DEFAULT_OUT_TIME = "defaultOutTime";
    /**
     * 默认重试次数设置
     */
    private static final String DEFAULT_RETRY_COUNT = "defaultRetryCount";
    /**
     * 服务端名称设置
     */
    private static final String SERVER_NAME = "serverName";
    /**
     * 服务端开启监听端口设置
     */
    private static final String MYRPC_PORT = "myRpcPort";

    private Properties properties;
    /**
     * zk注册中心地址
     */
    private String registrationCenterAddress;
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

        registrationCenterAddress = PropsUtil.getString(properties, REGISTRATION_CENTER_ADDRESS);
        String scanfPackagePath = PropsUtil.getString(properties, SCANF_PACKAGE_PATH);
        if (StringUtils.isNotBlank(scanfPackagePath)) {
            this.scanfPackagePaths = scanfPackagePath.split(PACKAGE_PATH_SPLIT);
        }
    }

    private void initClientProxyConfig() {
        Long defaultOutTime = PropsUtil.getLong(properties, DEFAULT_OUT_TIME);
        int defaultRetryCount = PropsUtil.getInt(properties, DEFAULT_RETRY_COUNT);
        clientProxyConfig = new ClientProxyConfig(defaultRetryCount, defaultOutTime);
    }

    private void initServerInfo() {
        String serverName = PropsUtil.getString(properties, SERVER_NAME);
        int myRpcPort = PropsUtil.getInt(properties, MYRPC_PORT);
        String address = SystemUtil.getIpAddress();
        serverInfo = new ServerInfo(serverName, address, myRpcPort);
    }

    public String getRegistrationCenterAddress() {
        return registrationCenterAddress;
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

    public static void main(String[] args) {
        MyRpcConfig rpcConfig = new MyRpcConfig("myrpc.properties");
    }
}
