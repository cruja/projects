package org.trading.orderbook.dispatch.command;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.session.commands.SessionCommand;
import org.trading.orderbook.session.SessionManager;

public class PlacedOrderCommand implements SessionCommand {

    private final OrderManager orderManager;

    private final SessionManager sessionManager;

    private final int id;

    public PlacedOrderCommand(
            SessionManager sessionHandler,
            OrderManager orderManager,
            int id) {
        this.sessionManager = sessionHandler;
        this.orderManager = orderManager;
        this.id = id;
    }

    @Override
    public void process() {
        Order order = orderManager.getOrderById(id);
        if (order != null) {
            //orderManager.placedOrder(id);
            JsonObject message = createMessage(order);
            sessionManager.dispatchToAllConnectedSessions(message);
        }
    }
    
    protected JsonObject createMessage(Order order) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "placed")
                .add("size", order.getSize())
                .add("id", order.getId())
                .build();
        return message;
    }
}
