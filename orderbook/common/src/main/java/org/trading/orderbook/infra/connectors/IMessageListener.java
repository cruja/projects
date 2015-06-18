package org.trading.orderbook.infra.connectors;


public interface IMessageListener {

    public void newMessage(String message);
}
