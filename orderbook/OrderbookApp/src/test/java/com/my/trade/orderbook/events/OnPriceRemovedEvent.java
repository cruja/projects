package com.my.trade.orderbook.events;


public class OnPriceRemovedEvent extends Event {

    private double price;

    public OnPriceRemovedEvent(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OnPriceRemovedEvent)) {
            return false;
        }

        return this.price == ((OnPriceRemovedEvent) obj).price;
    }
}
