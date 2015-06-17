package org.trading.orderbook.model;

import org.trading.orderbook.model.impl.side.OrdersPerSide;

public interface IModelObserver {

    public void onAdded(int sellOrdCnt, int sellAccVol, final double price, int buyAccVol, int buyOrdCnt);

    public void onPriceRemoved(final double price);

    public void onUpdated(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt);

    public void onUpdate(OrdersPerSide orderOrdersPerSide, double price);

    public void onChanged(OrdersPerSide ordersPerSide, double price);

    public void onFilled(int volume);

    public void onOrderAdded(long orderId, int size);

    public void onOrderFilled(long orderId, int volFilled);

    public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained);

    public void onOrderDeleted(long orderId);
}
