package org.trading.orderbook.infra.connectors;

import java.net.InetSocketAddress;

public class ActiveConnection extends Connection {

    public ActiveConnection(InetSocketAddress remote) {
        super(new CommunicationEndpoint(remote));
    }
}
