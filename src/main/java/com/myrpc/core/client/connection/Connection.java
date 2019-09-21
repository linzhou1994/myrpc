package com.myrpc.core.client.connection;

import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.server.ServerResponse;

public interface Connection {

    /**
     * 向服务端发送消息
     *
     * @param request
     * @return
     */
    ServerResponse sendMsg(ClientRequest request);

    /**
     * 当前连接是否活跃
     *
     * @return
     */
    boolean isUsable();

    /**
     * 关闭当前连接
     */
    void close();
}
