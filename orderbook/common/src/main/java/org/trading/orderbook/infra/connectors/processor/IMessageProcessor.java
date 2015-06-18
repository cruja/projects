package org.trading.orderbook.infra.connectors.processor;

public interface IMessageProcessor {

    public void start();

    public void processMessage(String message);

    public void enableAdvancing();
}
