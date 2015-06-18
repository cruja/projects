package org.trading.orderbook.connectors.processor;

import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.infra.connectors.processor.AbstractMessageProcessor;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractCommandProcessor extends AbstractMessageProcessor {

    protected abstract ICommandBuilder getCommandBuilder();

    public void processMessage(String message) {

        ICommandBuilder commandBuilder = getCommandBuilder();
        IMessageProcessingCommand command = commandBuilder.buildCommand(
                message, commandsQueue);

        try {
            commandsQueue.put(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(AbstractCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void enableAdvancing() {

    }
}
