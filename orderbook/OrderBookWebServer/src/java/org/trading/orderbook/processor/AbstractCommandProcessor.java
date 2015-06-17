package org.trading.orderbook.processor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.trading.orderbook.connectors.commands.AbstractCommandBuildCommand;
import org.trading.orderbook.connectors.commands.ICommandBuilder;

public abstract class AbstractCommandProcessor extends AbstractMessageProcessor {

    protected abstract ICommandBuilder getCommandBuilder();

    public void processMessage(String message) {

        ICommandBuilder commandBuilder = getCommandBuilder();
        AbstractCommandBuildCommand command = commandBuilder.buildCommand(
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
