package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.infra.connectors.Connection;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IPositionListener;
import org.trading.orderbook.model.impl.PositionsManager;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

public class OutgoingStream implements IModelObserver {

    private final Connection connection;

    private final IPositionListener listener;

    public OutgoingStream(Connection connection) {
        this.connection = connection;
        listener = new IPositionListener() {
            @Override
            public void positionChanged(double quantity, double amount) {
                String message = "{position;" + quantity + ";" + amount + "}";
                connection.sendMessage(message);
            }
        };
        PositionsManager.getInstance().register(listener);
    }

    @Override
    public void onAdded(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
    }

    @Override
    public void onPriceRemoved(double price) {
    }

    @Override
    public void onUpdated(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
    }

    @Override
    public void onUpdate(OrdersPerSide orderOrdersPerSide, double price) {
    }

    @Override
    public void onChanged(OrdersPerSide ordersPerSide, double price) {
    }

    @Override
    public void onFilled(int volume) {
    }

    @Override
    public void onOrderAdded(long orderId, int size) {
        String message = "{placed;" + orderId + ";" + size + "}";
        connection.sendMessage(message);
    }

    @Override
    public void onOrderFilled(long orderId, int volFilled) {
        String message = "{filled;" + orderId + ";" + volFilled + "}";
        connection.sendMessage(message);
    }

    @Override
    public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
        String message = "{partialfilled;" + orderId + ";" + (size - volRemained) + ";" + size + "}";
        connection.sendMessage(message);
    }

    @Override
    public void onOrderDeleted(long orderId) {
    }
}
