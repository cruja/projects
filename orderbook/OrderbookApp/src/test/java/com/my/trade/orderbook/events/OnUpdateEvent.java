package com.my.trade.orderbook.events;

import org.trading.orderbook.model.impl.side.OrdersPerSide;

public class OnUpdateEvent extends Event {
    OrdersPerSide orderOrdersPerSide;
    double price;

    public OnUpdateEvent(OrdersPerSide orderOrdersPerSide, double price) {
        this.orderOrdersPerSide = orderOrdersPerSide;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OnUpdateEvent)) {
            return false;
        }
        return this.orderOrdersPerSide.equals(((OnUpdateEvent) obj).orderOrdersPerSide) &&
                this.price == ((OnUpdateEvent) obj).price;
    }
}
