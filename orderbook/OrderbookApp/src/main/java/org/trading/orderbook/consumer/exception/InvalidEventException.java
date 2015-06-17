package org.trading.orderbook.consumer.exception;

public class InvalidEventException extends Exception {

    public InvalidEventException(String message) {
        super(message);
    }
}
