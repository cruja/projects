package org.trading.orderbook.connectors.upstream.commands;

import org.trading.orderbook.connectors.commands.AbstractBuildCommand;
import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderStream;
import org.trading.orderbook.model.impl.Action;
import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.connectors.processor.IMessageProcessingCommand;

import java.util.concurrent.BlockingQueue;

public class BuildCommand extends AbstractBuildCommand {

    public BuildCommand(
            String message,
            IOrderStream orderStream,
            IModelObserver callback,
            BlockingQueue<IMessageProcessingCommand> queue) {

        super(message, orderStream, callback, queue);
    }

    @Override
    public void process() {

        String[] split = message.split(";");
        try {
            String action = split[0];
            int orderId = Integer.parseInt(split[1]);
            String orderBook = split[2];
            boolean isBuy = "BUY".equals(split[3].toUpperCase());
            int volume = Integer.parseInt(split[4]);
            double size = Double.parseDouble(split[5]);

            switch (action) {
                case "add":
                    orderStream.publishEvent(
                            Action.ADDORDER,
                            new Order(orderId, orderBook, isBuy, size, volume),
                            callback);
                    break;
                case "place":
                    orderStream.publishEvent(
                            Action.ADDORDER,
                            new Order(orderId, orderBook, isBuy, size, volume),
                            callback);
                    break;
                case "cancel":
                    orderStream.publishEvent(
                            Action.CANCELORDER,
                            new Order(orderId, orderBook, isBuy, size, volume),
                            callback);
                    break;
                case "remove":
                    orderStream.publishEvent(
                            Action.DELETEORDER,
                            new Order(orderId, orderBook, isBuy, size, volume),
                            callback);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
