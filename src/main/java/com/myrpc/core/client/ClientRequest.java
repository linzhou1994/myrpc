package com.myrpc.core.client;

import com.myrpc.core.server.ServerResponse;

import java.io.Serializable;
import java.util.UUID;

public class ClientRequest implements Serializable {

    /**
     * 默认重试次数
     */
    private static final int DEFAULT_RETRY_COUNT = 0;
    /**
     * 默认请求超时时间
     */
    private static final long DEFAULT_TIME_OUT = 3000L;
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
    private String className;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private Object[] params;

    public ClientRequest() {
        this.uuid = UUID.randomUUID().toString();
        this.retryCount = DEFAULT_RETRY_COUNT;
        this.timeOut = DEFAULT_TIME_OUT;
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

    public String getClassName() {
        return className;
    }

    public String getMethod() {
        return method;
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

    public ClientRequest setClassName(String className) {
        this.className = className;
        return this;
    }

    public ClientRequest setMethod(String method) {
        this.method = method;
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
                ", response=" + response +
                '}';
    }
}
