package org.trading.orderbook.connectors.commands;

import java.util.concurrent.BlockingQueue;
import org.trading.orderbook.processor.IMessageProcessingCommand;
import org.trading.orderbook.dispatch.DispatcherManager;
import org.trading.orderbook.session.SessionManager;

public abstract class AbstractCommandBuildCommand implements IOrderCommand {

    protected final String message;

    protected final SessionManager sessionManager;

    protected final DispatcherManager dispatcherManager;

    protected final BlockingQueue<IMessageProcessingCommand> queue;

    public AbstractCommandBuildCommand(
            String message,
            SessionManager sessionManager,
            DispatcherManager dispatcherManager,
            BlockingQueue<IMessageProcessingCommand> queue) {
        
        this.sessionManager = sessionManager;
        this.dispatcherManager = dispatcherManager;
        this.message = message;
        this.queue = queue;
    }

    protected boolean isStringValid(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
