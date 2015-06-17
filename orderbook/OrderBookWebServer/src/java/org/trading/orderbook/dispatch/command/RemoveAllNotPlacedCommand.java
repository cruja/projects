package org.trading.orderbook.dispatch.command;

import java.util.Set;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.model.OrderManager;

import org.trading.orderbook.session.SessionManager;

public class RemoveAllNotPlacedCommand implements DispatchCommand {

    private final OrderManager orderManager;

    private final SessionManager sessionHandler;

    public RemoveAllNotPlacedCommand(
            SessionManager sessionHandler,
            OrderManager orderManager) {
        this.orderManager = orderManager;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void process() {
        Set<Integer> orderIds = orderManager.removeAllNotPLaced();
        orderIds.forEach((id) -> {
            JsonObject message = createMessage(id);
            sessionHandler.dispatchToAllConnectedSessions(message);
        });
    }

    protected JsonObject createMessage(int id) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "removed")
                .add("id", id)
                .build();
        return message;
    }
}
