package org.trading.orderbook.connectors.upstream;


import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.model.IOrderStream;
import org.trading.orderbook.connectors.processor.AbstractMessageProcessor;

public class UpstreamMessageProcessor extends AbstractMessageProcessor {

    private final IOrderStream orderStream;

    private final ICommandBuilder commandBuilder;

    public UpstreamMessageProcessor(IOrderStream orderStream, ICommandBuilder commandBuilder) {
        this.orderStream = orderStream;
        this.commandBuilder = commandBuilder;
        start();
    }

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }
}
