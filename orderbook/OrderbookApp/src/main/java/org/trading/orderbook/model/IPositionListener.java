package org.trading.orderbook.model;

public interface IPositionListener {

    public void positionChanged(double quantity, double amount);
}
