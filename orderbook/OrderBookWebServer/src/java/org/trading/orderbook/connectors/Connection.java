package org.trading.orderbook.connectors;

import java.nio.channels.AsynchronousSocketChannel;

public class Connection {

    private final CommunicationEndpoint endpoint;

    public Connection(AsynchronousSocketChannel channel, Object attachment) {
        endpoint = new CommunicationEndpoint(null, channel, attachment);
    }

    public void register(IMessageListener listener) {
        endpoint.register(listener);
    }
    
    public void notifyClient(String message) {
        endpoint.write(message);
    }

    public void read() {
        endpoint.read();
    }

}
