package org.trading.orderbook.infra.connectors.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractMessageProcessor implements IMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageProcessor.class);

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
                        LOGGER.error("", ex);
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
