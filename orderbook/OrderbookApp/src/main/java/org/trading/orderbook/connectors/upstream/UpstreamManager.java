package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.infra.connectors.ActiveConnection;
import org.trading.orderbook.infra.connectors.Connection;
import org.trading.orderbook.model.IOrderProcessorManager;
import org.trading.orderbook.model.IOrderProcessorManagerCompCtrl;
import org.trading.orderbook.model.IOrderStreamListener;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class UpstreamManager implements IOrderProcessorManagerCompCtrl {

    private final static UpstreamManager INSTANCE = new UpstreamManager();

    private final static InetSocketAddress REMOTE_ADDRESS = new InetSocketAddress("localhost", 28001);

    public static UpstreamManager getInstance() {
        return INSTANCE;
    }

    private Connection upstreamConnection;

    private IncomingOrderStream incomingStream;

    private OutgoingStream outgoingStream;

    private final Set<IOrderStreamListener> listeners;

    private UpstreamManager() {
        listeners = new HashSet<>();
    }

    @Override
    public void register(IOrderProcessorManager listener) {
        this.listeners.add(listener);
    }

    @Override
    public void start() {
        upstreamConnection = new ActiveConnection(REMOTE_ADDRESS);
        incomingStream = new IncomingOrderStream(upstreamConnection);
        listeners.forEach((listener) -> {
            incomingStream.register(listener);
        });
        outgoingStream = new OutgoingStream(upstreamConnection);
        try {
            incomingStream.openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }

    public OutgoingStream getOutgoingStream() {
        return outgoingStream;
    }
}
