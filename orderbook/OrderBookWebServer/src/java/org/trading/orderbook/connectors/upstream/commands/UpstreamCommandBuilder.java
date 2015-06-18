package org.trading.orderbook.connectors.upstream.commands;

import java.util.concurrent.BlockingQueue;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.trading.orderbook.connectors.commands.AbstractCommandBuildCommand;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.dispatch.DispatcherManager;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.session.SessionManager;

@ApplicationScoped
public class UpstreamCommandBuilder implements ICommandBuilder {

    @Inject
    private SessionManager sessionManager;

    @Inject
    private DispatcherManager dispatcherManager;

    @Override
    public AbstractCommandBuildCommand buildCommand(
            String message,
            BlockingQueue<IMessageProcessingCommand> queue) {

        return new CommandBuildCommand(
                message, sessionManager,
                dispatcherManager, queue);
    }

}
