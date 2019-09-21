package com.myrpc.core.server;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    private String uuid;

    private T rlt;

    private Throwable exception;

    public ServerResponse(String uuid) {
        this.uuid = uuid;
    }


    public String getUuid() {
        return uuid;
    }

    public T getRlt() {
        return rlt;
    }

    public Throwable getException() {
        return exception;
    }

    public void setRlt(T rlt) {
        this.rlt = rlt;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean isException() {
        return exception != null;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "uuid='" + uuid + '\'' +
                ", rlt=" + rlt +
                ", exception=" + exception +
                '}';
    }
}
