package org.trading.orderbook.connectors.upstream;

import org.trading.orderbook.env.EnvironmentInitializationException;

public class UpstreamInitializationException extends EnvironmentInitializationException {

    public UpstreamInitializationException(String message) {
        super(message);
    }
}
