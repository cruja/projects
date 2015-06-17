package org.trading.orderbook.dispatch.command;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.connectors.downstream.DownstreamCommandProcessor;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.session.SessionManager;

public class PlaceOrderCommand implements DispatchCommand {

    private final OrderManager orderManager;
    
    private final SessionManager sessionHandler;

    private final DownstreamCommandProcessor serverMessageProcessor;
    
    private final int id;

    public PlaceOrderCommand(
            SessionManager sessionHandler, 
            OrderManager orderManager,
            DownstreamCommandProcessor serverMessageProcessor,
            int id) {
        this.sessionHandler = sessionHandler;
        this.orderManager = orderManager;
        this.serverMessageProcessor = serverMessageProcessor;
        this.id = id;
    }

    @Override
    public void process() {
        Order order = orderManager.getOrderById(id);
        if (order != null) {
            orderManager.remove(order);
            orderManager.place(order);
            serverMessageProcessor.dispatchOrderPlaced(order);
            JsonObject message = createMessage(order);
            sessionHandler.dispatchToAllConnectedSessions(message);
        }
    }
    
    protected JsonObject createMessage(Order order) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "pendingplaced")                
                .add("id", order.getId())
                .build();
        return message;
    }
}
