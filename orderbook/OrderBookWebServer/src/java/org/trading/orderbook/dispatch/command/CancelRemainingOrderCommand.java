package org.trading.orderbook.dispatch.command;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.connectors.downstream.DownstreamMessageProcessor;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.session.SessionManager;

public class CancelRemainingOrderCommand implements DispatchCommand {

    private final int id;

    private final SessionManager sessionHandler;

    private final OrderManager orderManager;

    private final DownstreamMessageProcessor serverMessageProcessor;

    public CancelRemainingOrderCommand(
            SessionManager sessionHandler,
            OrderManager orderManager,
            DownstreamMessageProcessor serverMessageProcessor,
            int id) {
        this.id = id;
        this.sessionHandler = sessionHandler;
        this.orderManager = orderManager;
        this.serverMessageProcessor = serverMessageProcessor;
    }

    @Override
    public void process() {
        Order order = orderManager.getPlacedOrderById(id);
        if (order != null) {
            order.setSize(order.getFilled());
            serverMessageProcessor.dispatchOrderCancelled(order);
            JsonObject message = createMessage(order);
            sessionHandler.dispatchToAllConnectedSessions(message);
        }
    }
    
    protected JsonObject createMessage(Order order) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "cancelled")
                .add("id", order.getId())
                .add("book", order.getOrderBook())
                .add("size", order.getSize())
                .add("side", String.valueOf(order.getSide()))
                .build();
        return addMessage;
    }
}
