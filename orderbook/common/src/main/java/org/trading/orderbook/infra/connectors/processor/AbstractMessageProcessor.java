package org.trading.orderbook.infra.connectors.processor;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMessageProcessor implements IMessageProcessor {

    protected final BlockingQueue<IMessageProcessingCommand> commandsQueue;

    private final Runnable commandProcessor;

    private final Thread worker;

    protected AbstractMessageProcessor() {
        commandsQueue = new ArrayBlockingQueue<>(1000);
        commandProcessor = new Runnable() {

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
        worker = new Thread(commandProcessor);
    }

    public void start() {
        worker.start();
    }

    @Override
    public void enableAdvancing() {

    }
}
