package com.myrpc.core.client;

import com.myrpc.core.server.ServerResponse;

import java.io.Serializable;
import java.util.UUID;

public class ClientRequest implements Serializable {
    private String uuid;

    private ServerResponse response;

    public ClientRequest() {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public ServerResponse getResponse() {
        return response;
    }

    public void setResponse(ServerResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "uuid='" + uuid + '\'' +
                ", response=" + response +
                '}';
    }
}
