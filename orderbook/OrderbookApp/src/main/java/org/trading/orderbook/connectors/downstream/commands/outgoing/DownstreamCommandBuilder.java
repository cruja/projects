package org.trading.orderbook.connectors.downstream.commands.outgoing;

import org.trading.orderbook.connectors.commands.AbstractBuildCommand;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.model.IOrderStream;

import java.util.concurrent.BlockingQueue;

public class DownstreamCommandBuilder implements ICommandBuilder {

    private static final DownstreamCommandBuilder INSTANCE = new DownstreamCommandBuilder();

    public static DownstreamCommandBuilder getInstance() {
        return INSTANCE;
    }

    private DownstreamCommandBuilder() {

    }

    @Override
    public void setOrderStream(IOrderStream orderStream) {

    }

    @Override
    public AbstractBuildCommand buildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue) {

        return new BuildCommand(message, queue);
    }
}
