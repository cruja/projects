package org.trading.orderbook.connectors.commands;


import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.model.IOrderStream;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractBuildCommand implements IOrderCommand {

    protected final String message;

    protected IOrderStream orderStream;

    protected final BlockingQueue<IMessageProcessingCommand> queue;

    public AbstractBuildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue) {

        this.message = message;
        this.queue = queue;
    }

    public AbstractBuildCommand(
            String message,
            IOrderStream orderStream,
            BlockingQueue<IMessageProcessingCommand> queue) {
        this(message, queue);
        this.orderStream = orderStream;
    }
}
