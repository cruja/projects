package org.trading.orderbook.connectors.downstream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.connectors.ConnectionManager;
import org.trading.orderbook.connectors.IMessageListener;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.connectors.downstream.commands.DownstreamCommandBuilder;
import org.trading.orderbook.processor.AbstractCommandProcessor;

@Singleton
public class DownstreamCommandProcessor extends AbstractCommandProcessor {

    @Inject
    private DownstreamCommandBuilder commandBuilder;

    @Inject
    private ConnectionManager clientManager;

    private final IMessageListener messageListener; 

    public DownstreamCommandProcessor() {
        messageListener = new IMessageListener() {

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
        String message = order.serialize("place");
        clientManager.dispatchToAllConnections(message);
    }

    public void dispatchOrderCancelled(Order order) {
        String message = order.serialize("cancel");
        clientManager.dispatchToAllConnections(message);
    }

    public void dispatchOrderRemove(Order order) {
        String message = order.serialize("remove");
        clientManager.dispatchToAllConnections(message);
    }

    private void dispatchOrderAdd(Order order) {
        String message = order.serialize("add");
        clientManager.dispatchToAllConnections(message);
    }

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }

}
