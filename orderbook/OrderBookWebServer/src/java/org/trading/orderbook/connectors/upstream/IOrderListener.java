package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.model.Order;

public interface IOrderListener {

    public void placeOrder(Order order);
    
    public void addOrder(Order order);
    
    public void removeOrder(Order order);
    
    public void cancelOrder(Order order);
}
