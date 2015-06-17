package org.trading.orderbook.model;


import org.trading.orderbook.model.impl.level.PriceLevel;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

public interface IAggregatorObserver {

    public void onPriceLevelAdded(double price, PriceLevel priceLevel);

    public void onPriceLevelRemoved(double price);

    public void onPriceLevelUpdated(double price, PriceLevel priceLevel);

    public void onUpdate(OrdersPerSide orderOrdersPerSide, double price);
}
