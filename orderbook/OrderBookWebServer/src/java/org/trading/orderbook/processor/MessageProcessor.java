package org.trading.orderbook.processor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.trading.orderbook.session.SessionManager;
import org.trading.orderbook.connectors.commands.IOrderCommand;

public class MessageProcessor {

    @Inject
    private SessionManager sessionHandler;

    private final BlockingQueue<IOrderCommand> commandsQueue
            = new ArrayBlockingQueue<IOrderCommand>(1000);

    private final Runnable commandProcessor = new Runnable() {

        @Override
        public void run() {
            IOrderCommand command;
            while (true) {
                try {
                    command = commandsQueue.take();
                    command.process();
                } catch (Exception ex) {
                    Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    private final Thread worker = new Thread(commandProcessor);

    @PostConstruct
    public void onPostConstruct() {
        worker.start();
    }

}
