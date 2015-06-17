package org.trading.orderbook.model.impl;


import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

public class DefaultModelObserver implements IModelObserver {

    @Override
    public void onAdded(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
    }

    @Override
    public void onPriceRemoved(double price) {
    }

    @Override
    public void onUpdated(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
    }

    @Override
    public void onUpdate(OrdersPerSide orderOrdersPerSide, double price) {
    }

    @Override
    public void onChanged(OrdersPerSide ordersPerSide, double price) {
    }

    @Override
    public void onFilled(int volume) {
    }

    @Override
    public void onOrderAdded(long orderId, int size) {
    }

    @Override
    public void onOrderDeleted(long orderId) {
    }

    @Override
    public void onOrderFilled(long orderId, int volFilled) {
    }

    @Override
    public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
    }
}
