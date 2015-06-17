package org.trading.orderbook.connectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class ConnectionManager {

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    private IMessageListener incommingMessageListeners;    

    private final List<Connection> connections = new ArrayList<>();

    public ConnectionManager() {

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
            clientConnection.notifyClient(message);
        }
    }

    public void register(IMessageListener listener) {
        this.incommingMessageListeners = listener;
    }    
}
