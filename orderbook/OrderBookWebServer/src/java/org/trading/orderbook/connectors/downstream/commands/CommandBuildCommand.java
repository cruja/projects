package org.trading.orderbook.connectors.downstream.commands;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.trading.orderbook.connectors.commands.AbstractCommandBuildCommand;
import org.trading.orderbook.connectors.downstream.commands.incomming.OrderFilledCommand;
import org.trading.orderbook.connectors.downstream.commands.incomming.OrderPartiallyFilledCommand;
import org.trading.orderbook.connectors.downstream.commands.incomming.OrderPlacedCommand;
import org.trading.orderbook.processor.IMessageProcessingCommand;
import org.trading.orderbook.dispatch.DispatcherManager;
import org.trading.orderbook.session.SessionManager;

public class CommandBuildCommand extends AbstractCommandBuildCommand {

    private static final Logger LOGGER = Logger.getLogger(CommandBuildCommand.class.getName());

    public CommandBuildCommand(
            String message,
            SessionManager sessionManager,
            DispatcherManager dispatcherManager,
            BlockingQueue<IMessageProcessingCommand> queue) {

        super(message, sessionManager, dispatcherManager, queue);
    }

    @Override
    public void process() {
        String[] tokens = message.split(";");
        String action = tokens[0];
        switch (action) {
            case "placed":
                buildOrderPlacedCommand(tokens);
                break;
            case "filled":
                buildOrderFilledCommand(tokens);
                break;
            case "partialfilled":
                buildOrderPartiallyFilledCommand(tokens);
                break;
        }
    }

    private void buildOrderPlacedCommand(String[] tokens) {
        OrderPlacedCommand command = buildOrderPlaced(tokens);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building order placed command");
        }
    }

    private void buildOrderFilledCommand(String[] tokens) {
        OrderFilledCommand command = buildOrderFilled(tokens);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building order filled command");
        }
    }

    private void buildOrderPartiallyFilledCommand(String[] tokens) {
        OrderPartiallyFilledCommand command = buildOrderPartiallyFilled(tokens);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building order partially filled command");
        }
    }

    private OrderPartiallyFilledCommand buildOrderPartiallyFilled(String[] tokens) {

        if (tokens == null || tokens.length != 4
                || !isStringValid(tokens[1])
                || !isStringValid(tokens[2])
                || !isStringValid(tokens[3])) {

            return null;
        }

        try {
            int id = Integer.parseInt(tokens[1]);
            int filledSize = Integer.parseInt(tokens[2]);
            int size = Integer.parseInt(tokens[3]);

            return new OrderPartiallyFilledCommand(
                    sessionManager, id, filledSize, size);
        } catch (Exception e) {
            return null;
        }
    }

    private OrderFilledCommand buildOrderFilled(String[] tokens) {

        if (tokens == null || tokens.length != 3
                || !isStringValid(tokens[1])
                || !isStringValid(tokens[2])) {

            return null;
        }

        try {
            int id = Integer.parseInt(tokens[1]);
            int fillSize = Integer.parseInt(tokens[2]);

            return new OrderFilledCommand(
                    sessionManager, id, fillSize);
        } catch (Exception e) {
            return null;
        }
    }

    private OrderPlacedCommand buildOrderPlaced(String[] tokens) {

        if (tokens == null || tokens.length != 3
                || !isStringValid(tokens[1])
                || !isStringValid(tokens[2])) {

            return null;
        }

        try {
            int id = Integer.parseInt(tokens[1]);
            int size = Integer.parseInt(tokens[2]);

            return new OrderPlacedCommand(
                    sessionManager, id, size);
        } catch (Exception e) {
            return null;
        }
    }

}
