package org.trading.orderbook.dispatch;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.trading.orderbook.connectors.downstream.DownstreamMessageProcessor;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.OrderManager;
import org.trading.orderbook.dispatch.command.AddAndPlaceOrderCommand;
import org.trading.orderbook.dispatch.command.AddOrderCommand;
import org.trading.orderbook.dispatch.command.CancelRemainingOrderCommand;
import org.trading.orderbook.dispatch.command.PlaceOrderCommand;
import org.trading.orderbook.dispatch.command.RemoveAllNotPlacedCommand;
import org.trading.orderbook.dispatch.command.RemoveOrderCommand;
import org.trading.orderbook.session.SessionManager;

@Singleton
public class DispatcherManager {

    @Inject
    private DispatchCommandProcessor commandProcessor;

    @Inject
    private OrderManager orderManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private DownstreamMessageProcessor serverMessageProcessor;
    
    private volatile boolean isStarted;
    
    public void started(){
        isStarted = true;
    }

    public void addOrder(Order order) {
        commandProcessor.process(
                new AddOrderCommand(order, sessionManager, orderManager));
    }

    public void placeOrder(int id) {
        commandProcessor.process(
                new PlaceOrderCommand(
                        sessionManager,
                        orderManager,
                        serverMessageProcessor,
                        id));
    }

    public void addAndPlaceOrder(Order order) {
        commandProcessor.process(
                new AddAndPlaceOrderCommand(
                        sessionManager,
                        orderManager,
                        serverMessageProcessor,
                        order));
    }

    public void removeOrder(int id) {
        commandProcessor.process(
                new RemoveOrderCommand(
                        sessionManager,
                        orderManager,
                        id));
    }

    public void cancelRemainingOrder(int id) {
        commandProcessor.process(
                new CancelRemainingOrderCommand(
                        sessionManager,
                        orderManager,
                        serverMessageProcessor,
                        id));
    }

    public void removeAllNotPlacedCommand() {
        commandProcessor.process(
                new RemoveAllNotPlacedCommand(
                        sessionManager,
                        orderManager));
    }

}
