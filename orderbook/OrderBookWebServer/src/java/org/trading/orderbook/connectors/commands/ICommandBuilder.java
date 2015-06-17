package org.trading.orderbook.connectors.commands;

import java.util.concurrent.BlockingQueue;
import org.trading.orderbook.processor.IMessageProcessingCommand;

public interface ICommandBuilder {

    public AbstractCommandBuildCommand buildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue);
}
