package com.myrpc.core.client;

import com.myrpc.core.client.config.ClientProxyConfig;
import com.myrpc.core.server.ServerResponse;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

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
 * @描述: 客户端请求封装类
 */
public class ClientRequest implements Serializable {
    /**
     * 本次请求的唯一编码
     */
    private String uuid;
    /**
     * 服务端返回数据
     */
    private ServerResponse response;

    /**
     * 请求重试次数（这个是失败后的重试次数，如果为0那么不会重试）
     */
    private int retryCount;

    /**
     * 每次请求超时时间
     */
    private long timeOut;
    /**
     * 请求类名称
     */
    private String[] classNames;
    /**
     * 请求方法
     */
    private String methodName;
    /**
     * 请求参数
     */
    private Object[] params;

    public ClientRequest() {
        this.uuid = UUID.randomUUID().toString();
        this.retryCount = ClientProxyConfig.DEFAULT_RETRY_COUNT;
        this.timeOut = ClientProxyConfig.DEFAULT_TIME_OUT;
    }

    public String getUuid() {
        return uuid;
    }

    public ServerResponse getResponse() {
        return response;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public String[] getClassNames() {
        return classNames;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public ClientRequest setResponse(ServerResponse response) {
        this.response = response;
        return this;
    }

    public ClientRequest setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public ClientRequest setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public ClientRequest setClassNames(String[] classNames) {
        this.classNames = classNames;
        return this;
    }

    public ClientRequest setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public ClientRequest setParams(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "uuid='" + uuid + '\'' +
                ", retryCount=" + retryCount +
                ", timeOut=" + timeOut +
                ", classNames='" + classNames + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
