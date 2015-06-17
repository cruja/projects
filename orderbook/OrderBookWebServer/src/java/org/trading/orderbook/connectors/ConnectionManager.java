package org.trading.orderbook.connectors;

import org.trading.orderbook.connectors.downstream.DownstreamMessageListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.connectors.upstream.IOrderListener;

@Singleton
public class ConnectionManager {   

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    private final IOrderListener outgoingOrderListener;

    private DownstreamMessageListener incommingOrderListener;

    private final List<Connection> connections = new ArrayList<>();

    public ConnectionManager() {

        outgoingOrderListener = new IOrderListener() {

            @Override
            public void placeOrder(Order order) {
                notifyClientsOnOrderPlace(order);
            }

            @Override
            public void addOrder(Order order) {
                notifyClientsOnOrderAdd(order);
            }

            @Override
            public void removeOrder(Order order) {
                notifyClientsOnOrderRemove(order);
            }

            @Override
            public void cancelOrder(Order order) {
                notifyClientsOnOrderCancel(order);
            }
        };

        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            AsynchronousServerSocketChannel listener
                    = asynchronousServerSocketChannel.bind(new InetSocketAddress("localhost", 28001));

            System.out.println("Start listening for new connections:");
            listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    System.out.println("Accepted a new connection");

                    Connection client = new Connection(result, attachment);
                    client.register(incommingOrderListener);
                    connections.add(client);
                    client.read();

                    // accept the next connection
                    listener.accept(null, this);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("Failed accepting a new connection");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispatchOrderPlaced(Order order) {
        outgoingOrderListener.placeOrder(order);
    }

    public void dispatchOrderCancelled(Order order) {
        outgoingOrderListener.cancelOrder(order);
    }
     
    public void register(DownstreamMessageListener listener) {
        this.incommingOrderListener = listener;
    }

    private void notifyClientsOnOrderPlace(Order order) {
        for (Connection clientConnection : connections) {
            String message = order.serialize("place");
            clientConnection.notifyClient(message);
        }
    }

    private void notifyClientsOnOrderAdd(Order order) {
        for (Connection clientConnection : connections) {
            String message = order.serialize("add");
            clientConnection.notifyClient(message);
        }
    }

    private void notifyClientsOnOrderRemove(Order order) {
        for (Connection clientConnection : connections) {
            String message = order.serialize("remove");
            clientConnection.notifyClient(message);
        }
    }

    private void notifyClientsOnOrderCancel(Order order) {
        for (Connection clientConnection : connections) {
            String message = order.serialize("cancel");
            clientConnection.notifyClient(message);
        }
    }

}
