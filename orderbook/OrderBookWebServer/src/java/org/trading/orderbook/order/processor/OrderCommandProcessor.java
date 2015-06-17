package org.trading.orderbook.order.processor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.trading.orderbook.order.command.OrderCommand;
import org.trading.orderbook.session.processor.SessionCommandProcessor;

@ApplicationScoped
public class OrderCommandProcessor {

    private final BlockingQueue<OrderCommand> commandsQueue
            = new ArrayBlockingQueue<OrderCommand>(1000);

    private final Runnable commandProcessor = new Runnable() {

        @Override
        public void run() {
            OrderCommand command;
            while (true) {
                try {
                    command = commandsQueue.take();
                    command.process();
                } catch (Exception ex) {
                    Logger.getLogger(SessionCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
        }
    };

    private final Thread worker = new Thread(commandProcessor);

    @PostConstruct
    public void onPostConstruct() {
        worker.start();
    }

    public void process(OrderCommand command) {
        try {
            commandsQueue.put(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(SessionCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
