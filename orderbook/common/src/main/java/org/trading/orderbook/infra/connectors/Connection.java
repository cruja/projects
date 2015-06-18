package org.trading.orderbook.infra.connectors;

public class Connection {

    protected final CommunicationEndpoint endpoint;

    public Connection(CommunicationEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void register(IMessageListener listener) {
        endpoint.register(listener);
    }

    public void sendMessage(String message) {
        endpoint.write(message);
    }

    public void read() {
        endpoint.read();
    }
}
