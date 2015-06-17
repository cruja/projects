package org.trading.orderbook.connectors.processor;


import org.trading.orderbook.connectors.commands.AbstractBuildCommand;
import org.trading.orderbook.connectors.commands.ICommandBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMessageProcessor implements IMessageProcessor{

    protected final BlockingQueue<IMessageProcessingCommand> commandsQueue
            = new ArrayBlockingQueue<IMessageProcessingCommand>(1000);

    private final Runnable commandProcessor = new Runnable() {

        @Override
        public void run() {
            IMessageProcessingCommand command;
            while (true) {
                try {
                    command = commandsQueue.take();
                    command.process();
                } catch (Exception ex) {
                    Logger.getLogger(AbstractMessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    private final Thread worker = new Thread(commandProcessor);

    protected abstract ICommandBuilder getCommandBuilder();

    public void start() {
        worker.start();
    }

    @Override
    public void enableAdvancing() {

    }

    @Override
    public void processMessage(String message) {

        ICommandBuilder commandBuilder = getCommandBuilder();
        AbstractBuildCommand command = commandBuilder.buildCommand(
                message, commandsQueue);

        try {
            commandsQueue.put(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(AbstractMessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
