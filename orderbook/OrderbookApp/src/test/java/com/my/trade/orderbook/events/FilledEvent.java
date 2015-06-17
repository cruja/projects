package com.my.trade.orderbook.events;

public class FilledEvent extends Event {

    int volume;

    public FilledEvent(int volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FilledEvent)) {
            return false;
        }
        return this.volume == ((FilledEvent) obj).volume;
    }
}
