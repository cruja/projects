package org.trading.orderbook.model.impl.side;


import org.trading.orderbook.model.IModelObserver;

public class SellOrdersPerSide extends OrdersPerSide {

    public SellOrdersPerSide(IModelObserver observer) {
        super("Ask", 0, observer);
    }

    @Override
    public boolean isBuy() {
        return false;
    }

    @Override
    public double getBestPrice() {
        return getLowestPrice();
    }

    @Override
    public boolean isMatch(double price) {
        double bestPrice = getBestPrice();
        return bestPrice <= price;
    }
}
