package com.my.trade.orderbook.events;

public class OnAddedEvent extends Event {

    private double price;

    private int sellOrdCnt, sellAccVol, buyAccVol, buyOrdCnt;

    public OnAddedEvent(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
        this.sellAccVol = sellAccVol;
        this.sellOrdCnt = sellOrdCnt;
        this.buyAccVol = buyAccVol;
        this.buyOrdCnt = buyOrdCnt;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof OnAddedEvent)) {
            return false;
        }

        return
                this.price == ((OnAddedEvent) obj).price &&
                        this.sellOrdCnt == ((OnAddedEvent) obj).sellOrdCnt &&
                        this.sellAccVol == ((OnAddedEvent) obj).sellAccVol &&
                        this.buyAccVol == ((OnAddedEvent) obj).buyAccVol &&
                        this.buyOrdCnt == ((OnAddedEvent) obj).buyOrdCnt;
    }
}
