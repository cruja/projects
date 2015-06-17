package org.trading.orderbook.dispatch;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import org.trading.orderbook.dispatch.command.DispatchCommand;
import org.trading.orderbook.session.processor.SessionCommandProcessor;

@Singleton
public class DispatchCommandProcessor {

    private final BlockingQueue<DispatchCommand> commandsQueue
            = new ArrayBlockingQueue<DispatchCommand>(1000);

    private final Runnable commandProcessor = new Runnable() {

        @Override
        public void run() {
            DispatchCommand command;
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

    public void process(DispatchCommand command) {
        try {
            commandsQueue.put(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(SessionCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
