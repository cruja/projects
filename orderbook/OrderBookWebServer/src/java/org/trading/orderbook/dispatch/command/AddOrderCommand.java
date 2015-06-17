package org.trading.orderbook.dispatch.command;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.session.SessionManager;

public class AddOrderCommand implements DispatchCommand {

    private final Order order;

    private final OrderManager orderManager;

    private final SessionManager sessionManager;

    public AddOrderCommand(
            Order order,
            SessionManager sessionHandler,
            OrderManager orderManager) {
        this.order = order;
        this.sessionManager = sessionHandler;
        this.orderManager = orderManager;
    }

    @Override
    public void process() {
        int orderId = orderManager.getAndIncrementId();
        order.setId(orderId);
        orderManager.add(order);
        JsonObject message = createMessage(order);
        sessionManager.dispatchToAllConnectedSessions(message);
    }

    protected JsonObject createMessage(Order order) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "addo")
                .add("id", order.getId())
                .add("book", order.getOrderBook())
                .add("size", order.getSize())
                .add("price", order.getPrice())
                .add("side", String.valueOf(order.getSide()))
                .build();
        return addMessage;
    }
}
