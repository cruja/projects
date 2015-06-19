package org.trading.orderbook.infra.connectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

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

            LOGGER.info("Start listening for new connections on "
                    + binndingAddress.getHostName() + ":" + binndingAddress.getPort());

            listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    try {
                        LOGGER.info("Accepted a new connection from "
                                + String.valueOf(result.getRemoteAddress()));
                    } catch (IOException ex) {
                        LOGGER.error("", ex);
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
                    LOGGER.error("Failed accepting a new connection");
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
