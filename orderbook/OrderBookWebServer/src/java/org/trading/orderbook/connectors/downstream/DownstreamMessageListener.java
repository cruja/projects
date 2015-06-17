package org.trading.orderbook.connectors.downstream;

public interface DownstreamMessageListener {

    public void newMessage(String str);
}
