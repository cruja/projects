package org.trading.orderbook.session.processor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.trading.orderbook.session.commands.SessionCommand;

@ApplicationScoped
public class SessionCommandProcessor {

    private final BlockingQueue<SessionCommand> commandsQueue
            = new ArrayBlockingQueue<SessionCommand>(1000);

    private final Runnable commandProcessor = new Runnable() {

        @Override
        public void run() {
            SessionCommand command;
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

    public void process(SessionCommand command) {
        try {
            commandsQueue.put(command);
        } catch (InterruptedException ex) {
            Logger.getLogger(SessionCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
