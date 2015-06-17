package org.trading.orderbook.model;


public interface IdObserver {

    public void onAdded(long orderId);

    public void onDeleted(long orderId);
}
