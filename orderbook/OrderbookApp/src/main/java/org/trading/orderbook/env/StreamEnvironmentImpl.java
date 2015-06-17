package org.trading.orderbook.env;

import org.trading.orderbook.connectors.upstream.IncomingOrderStream;

public class StreamEnvironmentImpl extends EnvironmentImpl {

    public StreamEnvironmentImpl(String[] args) {
        super(args, new IncomingOrderStream());
    }

    @Override
    public final void run() {
        onStart();
        try {
            openStream();
        } catch (Exception e) {
            onStop();
        }
    }

}
