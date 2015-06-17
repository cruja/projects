package org.trading.orderbook.connectors.upstream.commands.incomming;

import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.dispatch.DispatcherManager;

public class PlaceOrderCommand implements IOrderCommand {

    private final int id;

    private final DispatcherManager dispatcherManager;

    public PlaceOrderCommand(
            DispatcherManager dispatcherManager,
            int id) {
        this.dispatcherManager = dispatcherManager;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void process() {
        int id = getId();
        dispatcherManager.placeOrder(id);
    }

}
