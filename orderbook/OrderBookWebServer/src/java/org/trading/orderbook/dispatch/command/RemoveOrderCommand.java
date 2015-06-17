package org.trading.orderbook.dispatch.command;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.session.SessionManager;

public class RemoveOrderCommand implements DispatchCommand {

    private final SessionManager sessionHandler;

    private final OrderManager orderManager;

    private final int id;

    public RemoveOrderCommand(
            SessionManager sessionHandler,
            OrderManager orderManager,
            int id) {
        this.sessionHandler = sessionHandler;
        this.orderManager = orderManager;
        this.id = id;
    }

    @Override
    public void process() {
        Order order = orderManager.getOrderById(id);
        if (order != null) {
            orderManager.remove(order);
            JsonObject message = createMessage(order);
            sessionHandler.dispatchToAllConnectedSessions(message);
        }
    }

    protected JsonObject createMessage(Order order) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "removed")
                .add("id", id)
                .build();
        return message;
    }
}
