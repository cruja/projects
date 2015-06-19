package org.trading.orderbook.model;

/**
 * Specifies the behavior of the application.
 */
public interface AppEnvironment {

    void registerProcessor(IOrderProcessor handler);

    void run();
}
