package org.trading.orderbook.connectors.downstream.commands.incomming;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.session.SessionManager;

public class PositionCommand implements IOrderCommand {

    private final SessionManager sessionManager;

    private final double quantity;

    private final double amount;

    public PositionCommand(
            SessionManager sessionManager,
            double quantity,
            double amount) {

        this.sessionManager = sessionManager;
        this.quantity = quantity;
        this.amount = amount;
    }

    @Override
    public void process() {
        JsonObject message = createPositionsMessage(quantity, amount);
        sessionManager.dispatchToAllConnectedSessions(message);
    }

    private JsonObject createPositionsMessage(double quantity, double amount) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "position")
                .add("quantity", quantity)
                .add("amount", amount)
                .build();

        return message;
    }

}
