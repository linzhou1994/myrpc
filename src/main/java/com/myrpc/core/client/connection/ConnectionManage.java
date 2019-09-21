package com.myrpc.core.client.connection;


import com.myrpc.core.client.ClientRequest;
import com.myrpc.core.common.handler.core.DefaultDecoder;
import com.myrpc.core.server.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManage {

    private static final Logger log = Logger.getLogger(ConnectionManage.class);

    private Map<String, Connection> address2Connection = new HashMap<>();


    public Connection getConnection(String address, int port) {
        if (StringUtils.isBlank(address) || port <= 0) return null;
        String key = address + "." + port;
        Connection connection = address2Connection.get(key);
        log.info("==========================ddress2Connection.getConnection:" + connection);
        if (connection == null || !connection.isUsable()) {
            connection = ClientConnection.createServerConnection(address, port);
        }
        return connection;
    }

    public static void main(String[] args) throws InterruptedException {
        sendMsg("127.0.0.1", 8888);
        sendMsg("127.0.0.1", 8888);
        sendMsg("127.0.0.1", 8880);


    }

    private static void sendMsg(String address, int port) throws InterruptedException {
        ConnectionManage connectionManage = new ConnectionManage();
        Connection connection = connectionManage.getConnection(address, port);
        log.info("==========================getConnection");
        if (connection != null) {
            Thread.sleep(3000);
            ClientRequest request = new ClientRequest();
            ServerResponse response = connection.sendMsg(request);
            log.info("==========================response1:" + response);
            request = new ClientRequest();
            response = connection.sendMsg(request);
            log.info("==========================response2:" + response);

        } else {
            log.info("==========================connection is null");
        }
    }


}
