package com.my.trade.orderbook.events;

public class OrderFilledEvent extends Event {

    long orderId;
    int volFilled;

    public OrderFilledEvent(long orderId, int volFilled) {
        this.orderId = orderId;
        this.volFilled = volFilled;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderFilledEvent)) {
            return false;
        }
        return this.orderId == ((OrderFilledEvent) obj).orderId &&
                this.volFilled == ((OrderFilledEvent) obj).volFilled;
    }
}
