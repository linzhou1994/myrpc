package com.myrpc.core.client.config;


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
 * @描述: 客户端代理请求服务端的配置类
 */
public class ClientProxyConfig {

    /**
     * 默认重试次数
     */
    public static final int DEFAULT_RETRY_COUNT = 0;
    /**
     * 默认请求超时时间
     */
    public static final long DEFAULT_TIME_OUT = 3000L;

    /**
     * 请求重试次数（这个是失败后的重试次数，如果为0那么不会重试）
     */
    private int retryCount;

    /**
     * 每次请求超时时间
     */
    private long timeOut;

    public ClientProxyConfig() {
        this.retryCount = ClientProxyConfig.DEFAULT_RETRY_COUNT;
        this.timeOut = ClientProxyConfig.DEFAULT_TIME_OUT;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }


}
