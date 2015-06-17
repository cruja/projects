package com.my.trade.orderbook.events;

public class OrderAddedEvent extends Event {

    private long orderId;

    public OrderAddedEvent(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderAddedEvent)) {
            return false;
        }
        return this.orderId == ((OrderAddedEvent) obj).orderId;
    }

}
