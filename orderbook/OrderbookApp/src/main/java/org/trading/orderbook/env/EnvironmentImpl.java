package org.trading.orderbook.env;

import java.util.logging.Logger;

import org.trading.orderbook.model.*;
import org.trading.orderbook.model.impl.ContextBuilder;
import org.trading.orderbook.model.impl.OrderProcessorManager;

public abstract class EnvironmentImpl implements AppEnvironment {

    private static Logger LOGGER = Logger.getLogger(EnvironmentImpl.class.getCanonicalName());

    protected final IContext context;

    protected final IOrderStream orderStream;

    private final IOrderProcessorManager orderProcessorManager;

    protected EnvironmentImpl(String[] args, IOrderStream orderStream) {
        this.orderProcessorManager = new OrderProcessorManager();
        this.orderStream = orderStream;
        this.context = ContextBuilder.getInstance().build(args);
        this.orderStream.setContext(this.context);
        this.orderStream.register(this.orderProcessorManager);
    }

    @Override
    public void registerProcessor(IOrderProcessor orderProcessor) {
        this.orderProcessorManager.registerProcessor(orderProcessor);
    }

    @Override
    public IContext getContext() {
        return context;
    }

    @Override
    public void run() {
        onStart();
        try {
            openStream();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onStop();
        }
    }

    /**
     * Sends a stream of orders to the {@link org.trading.orderbook.model.IOrderProcessor}s.
     *
     * @throws Exception if there is an error.
     */
    public void openStream() throws Exception {
        this.orderStream.openStream();
    }

    protected void onStart() {
        this.orderProcessorManager.onStart();
    }

    protected void onStop() {
        this.orderProcessorManager.onStop();
    }
}
