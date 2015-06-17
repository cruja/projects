package org.trading.orderbook.connectors.downstream.commands.incomming;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.session.SessionManager;

public class OrderPartiallyFilledCommand implements IOrderCommand {

    private final int id;

    private final int filledSize;

    private final int size;    
    
    private final SessionManager sessionManager;

    public OrderPartiallyFilledCommand(
            SessionManager sessionManager,
            int id,
            int filledSize,
            int size) {

        this.sessionManager = sessionManager;
        this.id = id;
        this.filledSize = filledSize;
        this.size = size;
    }

    @Override
    public void process() {
        JsonObject message = createFillOrderMessage(id, size, filledSize);
        sessionManager.dispatchToAllConnectedSessions(message);
    }

    private JsonObject createFillOrderMessage(int id, int size, int fillSize) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = null;
        if (fillSize >= size) {
            addMessage = provider.createObjectBuilder()
                    .add("action", "fill")
                    .add("id", id)
                    .add("filled", "yes")
                    .build();
        } else {
            addMessage = provider.createObjectBuilder()
                    .add("action", "fill")
                    .add("id", id)
                    .add("size", size)
                    .add("fill_size", fillSize)
                    .add("filled", "no")
                    .build();
        }
        return addMessage;
    }
}
