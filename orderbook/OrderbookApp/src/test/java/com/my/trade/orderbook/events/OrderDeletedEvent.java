package com.my.trade.orderbook.events;

public class OrderDeletedEvent extends Event {

    private long orderId;

    public OrderDeletedEvent(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderDeletedEvent)) {
            return false;
        }
        return this.orderId == ((OrderDeletedEvent) obj).orderId;
    }

}
