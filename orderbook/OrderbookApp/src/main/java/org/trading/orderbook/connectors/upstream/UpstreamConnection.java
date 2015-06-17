package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.connectors.CommunicationEndpoint;
import org.trading.orderbook.connectors.IIncomingMessageListener;

import java.net.InetSocketAddress;

public class UpstreamConnection {

    private final CommunicationEndpoint endpoint;

    public UpstreamConnection(InetSocketAddress remote) {
        endpoint = new CommunicationEndpoint(remote);
    }

    public void register(IIncomingMessageListener listener) {
        endpoint.register(listener);
    }

    public void sendMessage(String message) {
        endpoint.write(message);
    }
}
