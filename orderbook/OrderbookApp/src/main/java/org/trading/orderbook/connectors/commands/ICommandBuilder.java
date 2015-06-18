package org.trading.orderbook.connectors.commands;


import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.model.IOrderStream;

import java.util.concurrent.BlockingQueue;

public interface ICommandBuilder {

    public void setOrderStream(IOrderStream orderStream);

    public AbstractBuildCommand buildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue);
}
