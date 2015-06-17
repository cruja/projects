package org.trading.orderbook.model;

public interface IOrderProcessorManager extends IComponentController, IOrderStreamListener {

    void registerProcessor(IOrderProcessor handler);

}
