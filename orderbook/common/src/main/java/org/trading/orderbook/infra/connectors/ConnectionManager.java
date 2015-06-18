package org.trading.orderbook.connectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.trading.orderbook.infra.connectors.Connection;
import org.trading.orderbook.infra.connectors.IMessageListener;
import org.trading.orderbook.infra.connectors.PassiveConnection;

public class ConnectionManager {

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    private IMessageListener incommingMessageListeners;

    private final List<Connection> connections;

    public ConnectionManager() {
        connections = new ArrayList<>();
    }

    public void start(InetSocketAddress binndingAddress) {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            AsynchronousServerSocketChannel listener
                    = asynchronousServerSocketChannel.bind(binndingAddress);

            Logger.getLogger(ConnectionManager.class.getName()).log(
                    Level.INFO, "Start listening for new connections on "
                    + binndingAddress.getHostName() + ":" + binndingAddress.getPort());

            listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    try {
                        Logger.getLogger(ConnectionManager.class.getName()).log(
                                Level.INFO, "Accepted a new connection from "
                                + String.valueOf(result.getRemoteAddress()));
                    } catch (IOException ex) {
                        Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Connection client = new PassiveConnection(result, attachment);
                    client.register(incommingMessageListeners);
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

    public void dispatchToAllConnections(String message) {
        for (Connection clientConnection : connections) {
            clientConnection.sendMessage(message);
        }
    }

    public void register(IMessageListener listener) {
        this.incommingMessageListeners = listener;
    }
}
