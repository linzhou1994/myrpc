package com.myrpc.core.client.connection;

import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.server.ServerResponse;

public interface Connection {

    ServerResponse sendMsg(ClientRequest request);

    boolean isUsable();
}
