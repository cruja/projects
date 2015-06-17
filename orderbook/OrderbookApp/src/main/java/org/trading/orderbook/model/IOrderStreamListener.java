package org.trading.orderbook.model;

import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.impl.Action;
import org.trading.orderbook.model.impl.Order;

public interface IOrderStreamListener {

    void notifyEvent(Action action, Order order, IModelObserver callback) throws InvalidEventException;
}
