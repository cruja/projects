package org.trading.orderbook.infra.connectors;

import java.net.InetSocketAddress;

public class Connection {

    private final CommunicationEndpoint endpoint;

    public Connection(InetSocketAddress remote) {
        endpoint = new CommunicationEndpoint(remote);
    }

    public void register(IMessageListener listener) {
        endpoint.register(listener);
    }

    public void sendMessage(String message) {
        endpoint.write(message);
    }
}
