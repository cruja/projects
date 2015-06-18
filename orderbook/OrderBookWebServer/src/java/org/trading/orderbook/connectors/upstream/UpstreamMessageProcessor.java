package org.trading.orderbook.connectors.upstream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.trading.orderbook.connectors.upstream.commands.UpstreamCommandBuilder;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.order.command.AbstractCommandProcessor;
import org.trading.orderbook.session.SessionManager;

@Singleton
public class UpstreamMessageProcessor extends AbstractCommandProcessor {

    @Inject
    private SessionManager sessionHandler;

    @Inject
    private UpstreamCommandBuilder commandBuilder;

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }

    @PostConstruct
    public void onPostConstruct() {
        start();
    }

}
