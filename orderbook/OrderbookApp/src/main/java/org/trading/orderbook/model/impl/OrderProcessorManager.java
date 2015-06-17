package org.trading.orderbook.model.impl;

import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderProcessor;
import org.trading.orderbook.model.IOrderProcessorManager;

import java.util.LinkedHashSet;
import java.util.Set;

public class OrderProcessorManager implements IOrderProcessorManager {

    private final Set<IOrderProcessor> orderProcessors;

    public OrderProcessorManager() {
        orderProcessors = new LinkedHashSet<IOrderProcessor>();
    }

    @Override
    public void registerProcessor(IOrderProcessor handler) {
        orderProcessors.add(handler);
    }

    public void notifyEvent(Action action, Order order, IModelObserver callback) throws InvalidEventException {
        for (IOrderProcessor consumer : orderProcessors) {
            consumer.handleEvent(action, order, callback);
        }
    }

    @Override
    public void onStart() {
        for (IOrderProcessor consumer : orderProcessors) {
            consumer.startProcessing();
        }
    }

    @Override
    public void onStop() {
        for (IOrderProcessor consumer : orderProcessors) {
            consumer.finishProcessing();
        }
    }
}
