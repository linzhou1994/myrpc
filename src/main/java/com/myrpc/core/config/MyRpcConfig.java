package com.myrpc.core.config;

import com.myrpc.utils.PropsUtil;
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
     * 服务名称
     */
    private String serverName;
    /**
     * 服务端口
     */
    private String myRpcPort;
    /**
     * 默认请求超时时间（可以不填，不填则为3000ms）
     */
    private Long defaultOutTime;
    /**
     * 默认请求重试次数（以不填，不填则不重试）
     */
    private Integer defaultRetryCount;

    public MyRpcConfig(String fileName) {
        if (StringUtils.isBlank(fileName)){
            throw new IllegalArgumentException("config file name error!");
        }
        properties = PropsUtil.loadProps(fileName);

        zkAddress = PropsUtil.getString(properties,"zkAddress");

        serverName = PropsUtil.getString(properties,"serverName");
        myRpcPort = PropsUtil.getString(properties,"myRpcPort");
        defaultOutTime = PropsUtil.getLong(properties,"defaultOutTime");
        defaultRetryCount = PropsUtil.getInt(properties,"defaultRetryCount");

    }

    public String getZkAddress() {
        return zkAddress;
    }

    public String getServerName() {
        return serverName;
    }

    public String getMyRpcPort() {
        return myRpcPort;
    }

    public Long getDefaultOutTime() {
        return defaultOutTime;
    }

    public Integer getDefaultRetryCount() {
        return defaultRetryCount;
    }
}
