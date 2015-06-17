package org.trading.orderbook.connectors.downstream.commands;

import java.util.concurrent.BlockingQueue;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.trading.orderbook.connectors.commands.AbstractCommandBuildCommand;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.processor.IMessageProcessingCommand;
import org.trading.orderbook.dispatch.DispatcherManager;
import org.trading.orderbook.session.SessionManager;

@ApplicationScoped
public class DownstreamCommandBuilder implements ICommandBuilder {

    @Inject
    private SessionManager sessionManager;

    @Inject
    private DispatcherManager dispatcherManager;

    public AbstractCommandBuildCommand buildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue) {

        return new CommandBuildCommand(
                message, sessionManager,
                dispatcherManager, queue);
    }
}
