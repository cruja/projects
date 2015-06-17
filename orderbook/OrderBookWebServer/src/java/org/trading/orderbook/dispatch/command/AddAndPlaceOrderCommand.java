package org.trading.orderbook.dispatch.command;

import org.trading.orderbook.connectors.downstream.DownstreamMessageProcessor;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.session.SessionManager;

public class AddAndPlaceOrderCommand implements DispatchCommand {

    private final OrderManager orderManager;

    private final SessionManager sessionHandler;

    private final DownstreamMessageProcessor serverMessageProcessor;

    private final Order order;

    public AddAndPlaceOrderCommand(
            SessionManager sessionHandler,
            OrderManager orderManager,
            DownstreamMessageProcessor serverMessageProcessor,
            Order order) {
        this.sessionHandler = sessionHandler;
        this.orderManager = orderManager;
        this.serverMessageProcessor = serverMessageProcessor;
        this.order = order;
    }

    @Override
    public void process() {
        AddOrderCommand addCommand = new AddOrderCommand(
                order,
                sessionHandler,
                orderManager);

        addCommand.process();

        PlaceOrderCommand placeCommand = new PlaceOrderCommand(
                sessionHandler,
                orderManager,
                serverMessageProcessor,
                order.getId());

        placeCommand.process();
    }

}
