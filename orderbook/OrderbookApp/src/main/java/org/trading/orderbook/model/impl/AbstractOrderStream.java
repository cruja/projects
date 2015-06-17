package org.trading.orderbook.model.impl;

import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderStream;
import org.trading.orderbook.model.IOrderStreamListener;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractOrderStream implements IOrderStream {

    private final Set<IOrderStreamListener> listeners;

    public AbstractOrderStream() {
        listeners = new HashSet<>();
    }

    @Override
    public void register(IOrderStreamListener listener) {
        listeners.add(listener);
    }

    @Override
    public void publishEvent(Action action, Order order, IModelObserver callback) throws InvalidEventException {
        for (IOrderStreamListener listener : listeners) {
            listener.notifyEvent(action, order, callback);
        }
    }
}
