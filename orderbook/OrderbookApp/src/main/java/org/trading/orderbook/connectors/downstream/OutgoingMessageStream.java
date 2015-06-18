package org.trading.orderbook.connectors.downstream;

import org.trading.orderbook.connectors.downstream.commands.outgoing.DownstreamCommandBuilder;
import org.trading.orderbook.infra.connectors.Connection;

public class OutgoingMessageStream {

    private Connection upstreamConnection;

    private final DownstreamMessageProcessor messageProcessor;

    public OutgoingMessageStream() {

        messageProcessor = new DownstreamMessageProcessor(DownstreamCommandBuilder.getInstance());
    }

}
