package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.connectors.upstream.commands.UpstreamCommandBuilder;
import org.trading.orderbook.infra.connectors.ActiveConnection;
import org.trading.orderbook.infra.connectors.Connection;
import org.trading.orderbook.infra.connectors.IMessageListener;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.AbstractOrderStream;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

import java.net.InetSocketAddress;

public class IncomingOrderStream extends AbstractOrderStream {

    InetSocketAddress REMOTE_ADDRESS = new InetSocketAddress("localhost", 28001);

    private Connection upstreamConnection;

    private IMessageListener listener;

    private final UpstreamMessageProcessor messageProcessor;

    private final IModelObserver callback;

    public IncomingOrderStream() {
        callback = new IModelObserver() {
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
                upstreamConnection.sendMessage(message);
            }

            @Override
            public void onOrderFilled(long orderId, int volFilled) {
                String message = "{filled;" + orderId + ";" + volFilled + "}";
                upstreamConnection.sendMessage(message);
            }

            @Override
            public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
                String message = "{partialfilled;" + orderId + ";" + (size - volRemained) + ";" + size + "}";
                upstreamConnection.sendMessage(message);
            }

            @Override
            public void onOrderDeleted(long orderId) {
            }
        };
        UpstreamCommandBuilder.getInstance().setOrderStream(this);
        UpstreamCommandBuilder.getInstance().setCallback(callback);
        messageProcessor = new UpstreamMessageProcessor(
                this,
                UpstreamCommandBuilder.getInstance());
    }

    @Override
    public void openStream() throws Exception {
        upstreamConnection = new ActiveConnection(REMOTE_ADDRESS);

        listener = new IMessageListener() {
            @Override
            public void newMessage(String str) {
                messageProcessor.processMessage(str);
            }
        };

        upstreamConnection.register(listener);
    }
}
