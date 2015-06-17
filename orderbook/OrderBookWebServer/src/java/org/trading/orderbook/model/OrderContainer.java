package org.trading.orderbook.model;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class OrderContainer {

    private final AtomicInteger orderId = new AtomicInteger(0);

    private final ConcurrentMap<Integer, Order> preparedOrders = new ConcurrentHashMap<>();

    private final ConcurrentMap<Integer, Order> placedOrders = new ConcurrentHashMap<>();

    public Order getOrderById(int id) {
        return preparedOrders.get(id);
    }

    public Order getPlacedOrderById(int id) {
        return placedOrders.get(id);
    }

    public int getAndIncrementOrderId() {
        return orderId.getAndIncrement();
    }

    public void add(Order order) {
        preparedOrders.putIfAbsent(order.getId(), order);
    }

    public void remove(Order order) {
        preparedOrders.remove(order.getId());
    }

    public Set<Integer> removeAllNotPLaced() {
        Set<Integer> keySet = new HashSet<Integer>(preparedOrders.keySet());
        preparedOrders.clear();
        return keySet;
    }

    public void place(Order order) {
        placedOrders.putIfAbsent(order.getId(), order);
    }

    public void placed(int id) {
        Order order = placedOrders.remove(id);
        if (order != null) {
            order.setOrderState(OrderState.PLACED);
        }
    }
}
