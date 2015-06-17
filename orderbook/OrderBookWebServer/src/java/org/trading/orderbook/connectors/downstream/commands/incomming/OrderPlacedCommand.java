package org.trading.orderbook.connectors.downstream.commands.incomming;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.session.SessionManager;

public class OrderPlacedCommand implements IOrderCommand {

    private final int id;

    private final int size;
    
    private final SessionManager sessionManager;

    public OrderPlacedCommand(
            SessionManager sessionManager,
            int id,
            int size) {

        this.sessionManager = sessionManager;
        this.id = id;
        this.size = size;
    }

    @Override
    public void process() {
        JsonObject message = createPlacedOrderMessage(id, size);
        sessionManager.dispatchToAllConnectedSessions(message);
    }

    private JsonObject createPlacedOrderMessage(int id, int size) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "placed")
                .add("size", size)
                .add("id", id)
                .build();

        return message;
    }
}
