package org.trading.orderbook.env;

import org.trading.orderbook.connectors.upstream.UpstreamManager;

public class StreamEnvironmentImpl extends EnvironmentImpl {

    public StreamEnvironmentImpl(String[] args) throws EnvironmentInitializationException{
        super(args, UpstreamManager.getInstance(args));
    }
}
