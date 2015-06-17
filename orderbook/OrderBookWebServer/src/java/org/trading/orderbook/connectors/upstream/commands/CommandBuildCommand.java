package org.trading.orderbook.connectors.upstream.commands;

import org.trading.orderbook.connectors.upstream.commands.incomming.CancelOrderCommand;
import org.trading.orderbook.connectors.upstream.commands.incomming.RemoveOrderCommand;
import org.trading.orderbook.connectors.upstream.commands.incomming.PlaceOrderCommand;
import org.trading.orderbook.connectors.upstream.commands.incomming.NewOrderRangeCommand;
import org.trading.orderbook.connectors.upstream.commands.incomming.NewOrderCommand;
import java.io.StringReader;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.trading.orderbook.model.Side;
import org.trading.orderbook.connectors.commands.AbstractCommandBuildCommand;
import org.trading.orderbook.connectors.upstream.commands.incomming.RemoveAllNotPLacedCommand;
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
        try {
            StringReader stringReader = new StringReader(message);
            JsonReader reader = Json.createReader(stringReader);
            JsonObject jsonMessage = reader.readObject();
            String action = jsonMessage.getString("action").toLowerCase();
            switch (action) {
                case "addo":
                    buildAddOrderCommand(jsonMessage);
                    break;
                case "addor":
                    buildAddOrderRangeCommand(jsonMessage);
                    break;
                case "place":
                    buildPlaceOrderCommand(jsonMessage);
                    break;
                case "cancel":
                    buildCancelOrderCommand(jsonMessage);
                    break;
                case "remove":
                    buildRemoveOrderCommand(jsonMessage);
                    break;
                case "removeanp":
                    buildRemoveAllNotPlacedOrdersCommand(jsonMessage);
                    break;
            }
        } catch (Exception e) {

        }
    }

    private void buildAddOrderCommand(JsonObject jsonMessage) {
        NewOrderCommand command = buildAddOrderCommand(
                dispatcherManager, jsonMessage);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building add order command");
        }
    }

    private void buildAddOrderRangeCommand(JsonObject jsonMessage) {
        NewOrderRangeCommand command = buildAddOrderRangeCommand(
                dispatcherManager, jsonMessage);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building add order range command");
        }
    }

    private void buildPlaceOrderCommand(JsonObject jsonMessage) {
        PlaceOrderCommand command = buildPlaceOrderCommand(
                dispatcherManager, jsonMessage);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building place order command");
        }
    }

    private void buildCancelOrderCommand(JsonObject jsonMessage) {
        CancelOrderCommand command = buildCancelOrderCommand(
                dispatcherManager, jsonMessage);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building cancel remaining order command");
        }
    }

    private void buildRemoveOrderCommand(JsonObject jsonMessage) {
        RemoveOrderCommand command = buildRemoveOrderCommand(
                dispatcherManager, jsonMessage);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building remove order command");
        }
    }

    private void buildRemoveAllNotPlacedOrdersCommand(JsonObject jsonMessage) {
        RemoveAllNotPLacedCommand command = buildRemoveAllNotPlacedOrdersCommand(
                dispatcherManager, jsonMessage);
        if (command != null) {
            try {
                queue.put(command);
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "", ex);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Failed building remove order command");
        }
    }

    private NewOrderCommand buildAddOrderCommand(
            DispatcherManager dispatcherManager,
            JsonObject jsonMessage) {

        try {
            String bookStr = jsonMessage.getString("book");
            if (!isStringValid(bookStr)) {
                return null;
            }

            String sideStr = jsonMessage.getString("side");
            if (!isStringValid(sideStr)) {
                return null;
            }

            String sizeStr = jsonMessage.getString("size");
            if (!isStringValid(sizeStr)) {
                return null;
            }

            String priceStr = jsonMessage.getString("price");
            if (!isStringValid(priceStr)) {
                return null;
            }

            int size = Integer.parseInt(sizeStr);
            double price = Double.parseDouble(priceStr);
            Side side = Side.valueOf(sideStr);

            return new NewOrderCommand(
                    dispatcherManager, bookStr, side, size, price);

        } catch (Exception ex) {
            return null;
        }

    }

    private PlaceOrderCommand buildPlaceOrderCommand(
            DispatcherManager dispatcherManager,
            JsonObject jsonMessage) {

        try {
            int orderId = (int) jsonMessage.getInt("id");
            return new PlaceOrderCommand(dispatcherManager, orderId);
        } catch (Exception e) {
            return null;
        }
    }

    private CancelOrderCommand buildCancelOrderCommand(
            DispatcherManager dispatcherManager,
            JsonObject jsonMessage) {

        try {
            int orderId = (int) jsonMessage.getInt("id");
            return new CancelOrderCommand(dispatcherManager, orderId);
        } catch (Exception e) {
            return null;
        }
    }

    private RemoveOrderCommand buildRemoveOrderCommand(
            DispatcherManager dispatcherManagerr,
            JsonObject jsonMessage) {

        try {
            int orderId = (int) jsonMessage.getInt("id");
            return new RemoveOrderCommand(dispatcherManagerr, orderId);
        } catch (Exception e) {
            return null;
        }
    }

    private RemoveAllNotPLacedCommand buildRemoveAllNotPlacedOrdersCommand(
            DispatcherManager dispatcherManager,
            JsonObject jsonMessage) {

        try {
            return new RemoveAllNotPLacedCommand(dispatcherManager);
        } catch (Exception e) {
            return null;
        }
    }

    private NewOrderRangeCommand buildAddOrderRangeCommand(
            DispatcherManager dispatcherManager,
            JsonObject jsonMessage) {

        try {

            String book = jsonMessage.getString("book");
            if (!isStringValid(book)) {
                return null;
            }

            String sideStr = jsonMessage.getString("side");
            if (!isStringValid(sideStr)) {
                return null;
            }

            String sizeStr = jsonMessage.getString("size");
            if (!isStringValid(sizeStr)) {
                return null;
            }

            String priceStr = jsonMessage.getString("price");
            if (!isStringValid(priceStr)) {
                return null;
            }

            String directionStr = jsonMessage.getString("direction");
            if (!isStringValid(directionStr)) {
                return null;
            }

            String stepStr = jsonMessage.getString("step");
            if (!isStringValid(stepStr)) {
                return null;
            }

            String levelsStr = jsonMessage.getString("levels");
            if (!isStringValid(levelsStr)) {
                return null;
            }

            Side side = Side.valueOf(sideStr);
            int size = Integer.parseInt(sizeStr);
            int levels = Integer.parseInt(levelsStr);
            double step = Double.parseDouble(stepStr);
            double price = Double.parseDouble(priceStr);
            int direction = "UP".equals(directionStr) ? 1 : -1;

            return new NewOrderRangeCommand(
                    dispatcherManager, book, side, size,
                    levels, step, price, direction);

        } catch (Exception e) {
            return null;
        }
    }
}
