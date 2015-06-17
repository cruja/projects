package org.trading.orderbook.processor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMessageProcessor implements IMessageProcessor {

    protected final BlockingQueue<IMessageProcessingCommand> commandsQueue;    
    
    private final Runnable commandProcessor;

    private final Thread worker;

    protected AbstractMessageProcessor() {                
        commandsQueue = new ArrayBlockingQueue<IMessageProcessingCommand>(1000);        
        commandProcessor = new Runnable() {

            @Override
            public void run() {
                IMessageProcessingCommand command;
                while (true) {
                    try {                                                
                        command = commandsQueue.take();
                        command.process();
                    } catch (Exception ex) {
                        Logger.getLogger(AbstractCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
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
    public abstract void processMessage(String message);
}
