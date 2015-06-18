package org.trading.orderbook.connectors.downstream;

import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.connectors.processor.AbstractCommandProcessor;

public class DownstreamMessageProcessor extends AbstractCommandProcessor {

    private final ICommandBuilder commandBuilder;

    public DownstreamMessageProcessor(ICommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
        start();
    }

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }
}
