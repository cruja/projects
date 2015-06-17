package org.trading.orderbook.connectors.commands;


import org.trading.orderbook.model.IOrderStream;
import org.trading.orderbook.connectors.processor.IMessageProcessingCommand;

import java.util.concurrent.BlockingQueue;

public interface ICommandBuilder {

    public void setOrderStream(IOrderStream orderStream);

    public AbstractBuildCommand buildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue);
}
