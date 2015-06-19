package org.trading.orderbook.connectors.upstream;


import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.connectors.processor.AbstractCommandProcessor;
import org.trading.orderbook.model.IOrderStream;

public class UpstreamCommandProcessor extends AbstractCommandProcessor {

    private final ICommandBuilder commandBuilder;

    public UpstreamCommandProcessor(ICommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
        start();
    }

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }
}
