package org.trading.orderbook.infra.connectors;

import java.nio.channels.AsynchronousSocketChannel;

public class PassiveConnection extends Connection {

    public PassiveConnection(AsynchronousSocketChannel channel, Object attachment) {
        super(new CommunicationEndpoint(null, channel, attachment));
    }

}
