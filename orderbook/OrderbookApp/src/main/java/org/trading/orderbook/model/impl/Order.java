package org.trading.orderbook.model.impl;

/**
 * A Data transfer object used by {@link org.trading.orderbook.model.AppEnvironment} to send data used.
 */
public class Order {

    private final long orderId;

    private final String orderBook;

    private final boolean isBuy;

    private final double price;

    private int volume;

    private final int size;

    public long getOrderId() {
        return orderId;
    }

    public Order(long orderId, String orderBook, boolean isBuy, double price,
                 int volume) {
        this.orderId = orderId;
        this.orderBook = orderBook;
        this.isBuy = isBuy;
        this.price = price;
        this.volume = volume;
        this.size = volume;
    }

    public String getOrderBook() {
        return orderBook;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        return (int) orderId;
    }

    @Override
    public boolean equals(Object obj) {
        return orderId == ((Order) obj).orderId;
    }
}
