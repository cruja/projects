package org.trading.orderbook.model;

public interface IOrderBook extends IOrderConsumerComponent {

    public void register(IModelObserver modelObserver);

    public void registerForAggregator(IAggregatorObserver aggregatorObserver);

    public double getLowestPrice(boolean isBuySide);

    public double getHighestPrice(boolean isBuySide);

    public double getBestPrice(boolean isBuySide);

    public int getAccVol(boolean isBuySide);

    public int getOrdersCount(boolean isBuySide);

    public int getOrdersCount(double price, boolean isBuySide);

    public int getAccVolume(double price, boolean isBuySide);
}
