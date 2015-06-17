package com.my.trade.orderbook.events;

public class OnUpdatedEvent extends Event {

    private double price;

    private int sellOrdCnt, sellAccVol, buyAccVol, buyOrdCnt;

    public OnUpdatedEvent(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
        this.sellAccVol = sellAccVol;
        this.sellOrdCnt = sellOrdCnt;
        this.buyAccVol = buyAccVol;
        this.buyOrdCnt = buyOrdCnt;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof OnUpdatedEvent)) {
            return false;
        }

        return
                this.price == ((OnUpdatedEvent) obj).price &&
                        this.sellOrdCnt == ((OnUpdatedEvent) obj).sellOrdCnt &&
                        this.sellAccVol == ((OnUpdatedEvent) obj).sellAccVol &&
                        this.buyAccVol == ((OnUpdatedEvent) obj).buyAccVol &&
                        this.buyOrdCnt == ((OnUpdatedEvent) obj).buyOrdCnt;
    }
}
