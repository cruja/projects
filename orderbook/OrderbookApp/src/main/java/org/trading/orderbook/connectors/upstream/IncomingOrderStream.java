package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.connectors.upstream.commands.UpstreamCommandBuilder;
import org.trading.orderbook.infra.connectors.Connection;
import org.trading.orderbook.infra.connectors.IMessageListener;
import org.trading.orderbook.model.impl.AbstractOrderStream;

public class IncomingOrderStream extends AbstractOrderStream {

    private final Connection connection;

    private IMessageListener listener;

    private final UpstreamCommandProcessor commandProcessor;

    public IncomingOrderStream(Connection connection) {
        this.connection = connection;
        UpstreamCommandBuilder.getInstance().setOrderStream(this);
        commandProcessor = new UpstreamCommandProcessor(
                UpstreamCommandBuilder.getInstance());
    }

    @Override
    public void openStream() throws Exception {

        listener = new IMessageListener() {
            @Override
            public void newMessage(String str) {
                commandProcessor.processMessage(str);
            }
        };

        connection.register(listener);
    }
}
