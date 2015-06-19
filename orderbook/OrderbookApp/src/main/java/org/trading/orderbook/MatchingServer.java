package org.trading.orderbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trading.orderbook.env.StreamEnvironmentImpl;
import org.trading.orderbook.consumer.OrderBookDispatcher;
import org.trading.orderbook.model.AppEnvironment;


public class MatchingServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingServer.class);

    public static void main(String[] args) {

        LOGGER.info("Starting order book server");

        try {
            AppEnvironment environment = new StreamEnvironmentImpl(args);
            OrderBookDispatcher consumer = new OrderBookDispatcher();
            environment.registerProcessor(consumer);
            environment.run();
        } catch (Exception e) {
            LOGGER.error("Failed starting the application", e.getMessage(), e);
        }
    }
}
