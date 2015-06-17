package org.trading.orderbook.model;

import org.trading.orderbook.model.impl.Order;

public interface IOrderConsumer {

    public void add(Order order);

    public void delete(Order order);

    public void cancel(Order order);
}
