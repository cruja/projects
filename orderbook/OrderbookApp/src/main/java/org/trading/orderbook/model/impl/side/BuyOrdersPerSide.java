package org.trading.orderbook.model.impl.side;

import org.trading.orderbook.model.IModelObserver;

public class BuyOrdersPerSide extends OrdersPerSide {

    public BuyOrdersPerSide(IModelObserver observer) {
        super("Bid", 1, observer);
    }

    @Override
    public boolean isBuy() {
        return true;
    }

    @Override
    public double getBestPrice() {
        return getHighestPrice();
    }

    @Override
    public boolean isMatch(double price) {
        double bestPrice = getBestPrice();
        return bestPrice >= price;
    }
}
