package org.trading.orderbook.connectors.downstream.commands.outgoing;

import org.trading.orderbook.connectors.commands.AbstractBuildCommand;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;

import java.util.concurrent.BlockingQueue;

public class BuildCommand extends AbstractBuildCommand {

    public BuildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue) {
        super(message, queue);
    }

    @Override
    public void process() {

    }
}
