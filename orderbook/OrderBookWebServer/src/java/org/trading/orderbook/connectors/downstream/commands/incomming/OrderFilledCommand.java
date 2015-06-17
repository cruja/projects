package org.trading.orderbook.connectors.downstream.commands.incomming;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.session.SessionManager;

public class OrderFilledCommand implements IOrderCommand {

    private final int id;
    
    private final int filledSize;

    private final SessionManager sessionManager;
    
    public OrderFilledCommand(
            SessionManager sessionManager,
            int id,
            int filledSize) {

        this.sessionManager = sessionManager;
        this.id = id;
        this.filledSize = filledSize;
    }

    @Override
    public void process() {
        JsonObject message = createFillOrderMessage(id, filledSize, filledSize);
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
