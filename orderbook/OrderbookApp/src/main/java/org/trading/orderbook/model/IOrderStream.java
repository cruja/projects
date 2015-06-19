package org.trading.orderbook.model;

import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.impl.Action;
import org.trading.orderbook.model.impl.Order;

public interface IOrderStream {

    void register(IOrderStreamListener listener);

    void openStream() throws Exception;

    public void publishEvent(Action action, Order order)
            throws InvalidEventException;
}
