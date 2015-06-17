package org.trading.orderbook;

import org.trading.orderbook.env.StreamEnvironmentImpl;
import org.trading.orderbook.consumer.OrderBookDispatcher;
import org.trading.orderbook.model.AppEnvironment;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppRunner {

    private static Logger LOGGER = Logger.getLogger(AppRunner.class.getCanonicalName());

    public static void main(String[] args) {

        try {
            AppEnvironment environment = new StreamEnvironmentImpl(args);
            OrderBookDispatcher consumer = new OrderBookDispatcher(environment.getContext());
            environment.registerProcessor(consumer);
            environment.run();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed starting the application", e);
        }
    }
}
