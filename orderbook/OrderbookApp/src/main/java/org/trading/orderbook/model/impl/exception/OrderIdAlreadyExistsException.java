package org.trading.orderbook.model.impl.exception;

public class OrderIdAlreadyExistsException extends RuntimeException {

    public OrderIdAlreadyExistsException(String message) {
        super(message);
    }
}
