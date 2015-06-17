package org.trading.orderbook.model.impl;

import org.trading.orderbook.model.IdContainer;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class OrderIdContainer implements IdContainer {

    private final TreeSet<Long> orderIds;

    public OrderIdContainer() {
        orderIds = new TreeSet<Long>();
    }

    @Override
    public void onAdded(long orderId) {
        orderIds.add(orderId);
    }

    @Override
    public void onDeleted(long orderId) {
        orderIds.remove(orderId);
    }

    public boolean contains(long orderId) {
        return orderIds.contains(orderId);
    }

    public List<Long> getOrderIds() {
        return Arrays.asList(orderIds.toArray(new Long[0]));
    }
}
