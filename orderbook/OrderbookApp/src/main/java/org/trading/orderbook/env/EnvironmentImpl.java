package org.trading.orderbook.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trading.orderbook.model.*;
import org.trading.orderbook.model.impl.OrderProcessorManager;

public abstract class EnvironmentImpl implements AppEnvironment {

    private static Logger LOGGER = LoggerFactory.getLogger(EnvironmentImpl.class);

    private final IOrderProcessorManagerCompCtrl controller;

    private final IOrderProcessorManager orderProcessorManager;

    protected EnvironmentImpl(String[] args, IOrderProcessorManagerCompCtrl controller) {
        this.controller = controller;
        this.orderProcessorManager = new OrderProcessorManager();
        this.controller.register(this.orderProcessorManager);
    }

    @Override
    public void registerProcessor(IOrderProcessor orderProcessor) {
        this.orderProcessorManager.registerProcessor(orderProcessor);
    }

    @Override
    public void run() {
        try {
            controller.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
