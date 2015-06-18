package org.trading.orderbook.connectors.downstream;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.connectors.downstream.commands.DownstreamCommandBuilder;
import org.trading.orderbook.infra.connectors.ConnectionManager;
import org.trading.orderbook.infra.connectors.IMessageListener;
import org.trading.orderbook.order.command.AbstractCommandProcessor;

@Singleton
public class DownstreamCommandProcessor extends AbstractCommandProcessor {

    @Inject
    private DownstreamCommandBuilder commandBuilder;
    
    private final ConnectionManager connectionManager;

    private final IMessageListener messageListener;

    private InetSocketAddress binndingAddress;

    public DownstreamCommandProcessor() {
        connectionManager = new ConnectionManager();
        int listeningPort = 28001;
        String listeningHostName = "localhost";

        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            listeningPort = (Integer) env.lookup("downstream.listening.port");
            listeningHostName = (String) env.lookup("downstream.listening.hostname");

        } catch (Exception ex) {
            Logger.getLogger(DownstreamCommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            binndingAddress = new InetSocketAddress(listeningHostName, listeningPort);
        }

        messageListener = new IMessageListener() {

            @Override
            public void newMessage(String str) {
                processMessage(str);
            }
        };
    }

    @PostConstruct
    public void onPostConstruct() {
        start();
        connectionManager.register(messageListener);        
        connectionManager.start(binndingAddress);
    }

    public void dispatchOrderPlaced(Order order) {
        String message = order.serialize("place");
        connectionManager.dispatchToAllConnections(message);
    }

    public void dispatchOrderCancelled(Order order) {
        String message = order.serialize("cancel");
        connectionManager.dispatchToAllConnections(message);
    }

    public void dispatchOrderRemove(Order order) {
        String message = order.serialize("remove");
        connectionManager.dispatchToAllConnections(message);
    }

    private void dispatchOrderAdd(Order order) {
        String message = order.serialize("add");
        connectionManager.dispatchToAllConnections(message);
    }

    @Override
    protected ICommandBuilder getCommandBuilder() {
        return commandBuilder;
    }

}
