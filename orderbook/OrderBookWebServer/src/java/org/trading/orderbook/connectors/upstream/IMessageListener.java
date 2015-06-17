package org.trading.orderbook.connectors.upstream;

public interface IMessageListener {

    public void newMessage(String message);
}
