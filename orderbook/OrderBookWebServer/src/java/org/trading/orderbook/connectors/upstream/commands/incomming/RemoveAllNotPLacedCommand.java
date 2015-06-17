package org.trading.orderbook.connectors.upstream.commands.incomming;

import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.dispatch.DispatcherManager;

public class RemoveAllNotPLacedCommand implements IOrderCommand {

    private final DispatcherManager dispatcherManager;

    public RemoveAllNotPLacedCommand(DispatcherManager dispatcherManager) {
        this.dispatcherManager = dispatcherManager;
    }

    @Override
    public void process() {
        dispatcherManager.removeAllNotPlacedCommand();
    }

}
