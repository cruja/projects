package org.trading.orderbook.model.impl;

import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderProcessor;
import org.trading.orderbook.model.IOrderProcessorManager;
import org.trading.orderbook.model.IOrderStreamListener;

import java.util.LinkedHashSet;
import java.util.Set;

public class OrderProcessorManager implements IOrderProcessorManager {

    private final Set<IOrderProcessor> orderProcessors;

    public OrderProcessorManager() {
        orderProcessors = new LinkedHashSet<>();
    }

    @Override
    public void registerProcessor(IOrderProcessor handler) {
        orderProcessors.add(handler);
    }

    @Override
    public void notifyEvent(Action action, Order order) throws InvalidEventException {
        for (IOrderProcessor consumer : orderProcessors) {
            consumer.handleEvent(action, order);
        }
    }

    @Override
    public void start() {
        for (IOrderProcessor consumer : orderProcessors) {
            consumer.startProcessing();
        }
    }

    @Override
    public void stop() {
        for (IOrderProcessor consumer : orderProcessors) {
            consumer.finishProcessing();
        }
    }

}
