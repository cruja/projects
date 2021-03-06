package org.trading.orderbook.connectors.upstream.commands;

import org.trading.orderbook.connectors.commands.AbstractBuildCommand;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.model.IOrderStream;

import java.util.concurrent.BlockingQueue;

public class UpstreamCommandBuilder implements ICommandBuilder {

    private static final UpstreamCommandBuilder INSTANCE = new UpstreamCommandBuilder();

    public static UpstreamCommandBuilder getInstance() {
        return INSTANCE;
    }

    private IOrderStream orderStream;

    private UpstreamCommandBuilder() {

    }

    @Override
    public void setOrderStream(IOrderStream orderStream) {
        this.orderStream = orderStream;
    }

    @Override
    public AbstractBuildCommand buildCommand(String message, BlockingQueue<IMessageProcessingCommand> queue) {

        return new BuildCommand(message, orderStream, queue);
    }
}
