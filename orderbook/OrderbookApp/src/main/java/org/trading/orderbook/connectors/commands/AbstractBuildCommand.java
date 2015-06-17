package org.trading.orderbook.connectors.commands;


import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderStream;
import org.trading.orderbook.connectors.processor.IMessageProcessingCommand;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractBuildCommand implements IOrderCommand {

    protected final String message;

    protected final IOrderStream orderStream;

    protected final IModelObserver callback;

    protected final BlockingQueue<IMessageProcessingCommand> queue;

    public AbstractBuildCommand(
            String message,
            IOrderStream orderStream,
            IModelObserver callback,
            BlockingQueue<IMessageProcessingCommand> queue) {

        this.message = message;
        this.orderStream = orderStream;
        this.callback = callback;
        this.queue = queue;
    }
}
