package org.trading.orderbook.model;

import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.trading.orderbook.order.processor.OrderCommandProcessor;

@Singleton
public class OrderManager {

    @Inject
    private OrderContainer orderContainer;

    @Inject
    private OrderCommandProcessor orderCommandProcessor;

    public Order getOrderById(int id) {
        return orderContainer.getOrderById(id);
    }

    public Order getPlacedOrderById(int id) {
        return orderContainer.getPlacedOrderById(id);
    }

    public int getAndIncrementId() {
        return orderContainer.getAndIncrementOrderId();
    }

    public void add(Order order) {
        orderContainer.add(order);
    }

    public void remove(Order order) {
        orderContainer.remove(order);
    }

    public Set<Integer> removeAllNotPLaced() {
        return orderContainer.removeAllNotPLaced();
    }

    public void place(Order order) {
        orderContainer.place(order);
    }

    public void placed(int id) {
        orderContainer.placed(id);
    }
}
