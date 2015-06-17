package org.trading.orderbook.connectors.downstream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.connectors.ConnectionManager;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.connectors.downstream.commands.DownstreamCommandBuilder;
import org.trading.orderbook.processor.AbstractCommandProcessor;

@Singleton
public class DownstreamMessageProcessor extends AbstractCommandProcessor {

    @Inject
    private DownstreamCommandBuilder commandBuilder;

    @Inject
    private ConnectionManager clientManager;

    private final DownstreamMessageListener messageListener;

    public DownstreamMessageProcessor() {
        messageListener = new DownstreamMessageListener() {

            @Override
            public void newMessage(String str) {
                processMessage(str);
            }
        };              
    }

    @PostConstruct
    public void onPostConstruct() {
        clientManager.register(messageListener);
        start();
    }

    public void dispatchOrderPlaced(Order order) {
        clientManager.dispatchOrderPlaced(order);
    }

    public void dispatchOrderCancelled(Order order) {
        clientManager.dispatchOrderCancelled(order);
    }

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }
}
