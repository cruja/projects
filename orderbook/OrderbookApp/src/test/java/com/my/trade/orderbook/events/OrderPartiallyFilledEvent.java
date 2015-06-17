package com.my.trade.orderbook.events;

public class OrderPartiallyFilledEvent extends Event {

    long orderId;
    int volFilled, volRemained;

    public OrderPartiallyFilledEvent(long orderId, int volFilled, int volRemained) {
        this.orderId = orderId;
        this.volFilled = volFilled;
        this.volRemained = volRemained;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderPartiallyFilledEvent)) {
            return false;
        }
        return this.orderId == ((OrderPartiallyFilledEvent) obj).orderId &&
                this.volFilled == ((OrderPartiallyFilledEvent) obj).volFilled &&
                this.volRemained == ((OrderPartiallyFilledEvent) obj).volRemained;
    }
}
