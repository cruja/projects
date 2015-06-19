package org.trading.orderbook.gui;


import org.trading.orderbook.gui.model.OrderBookModel;
import org.trading.orderbook.model.impl.level.PriceLevel;
import org.trading.orderbook.model.IAggregatorListener;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

public class OrderBookGui implements IAggregatorListener {

    private final OrderBookModel orderBookModel;

    private final OrderBookFrame orderBookFrame;

    public OrderBookGui(String name) {
        orderBookModel = new OrderBookModel();
        orderBookFrame = new OrderBookFrame(name, orderBookModel);
    }

    @Override
    public void onPriceLevelAdded(double price, PriceLevel priceLevel) {
        orderBookModel.onAdded(priceLevel.getOrdCnt(0), priceLevel.getAccVol(0), price, priceLevel.getAccVol(1), priceLevel.getOrdCnt(1));
    }

    @Override
    public void onPriceLevelRemoved(double price) {
        orderBookModel.onPriceRemoved(price);
    }

    @Override
    public void onPriceLevelUpdated(double price, PriceLevel priceLevel) {
        orderBookModel.onUpdated(priceLevel.getOrdCnt(0), priceLevel.getAccVol(0), price, priceLevel.getAccVol(1), priceLevel.getOrdCnt(1));
    }

    @Override
    public void onUpdate(OrdersPerSide orderOrdersPerSide, double price) {
        orderBookModel.onUpdate(orderOrdersPerSide, price);
    }
}
