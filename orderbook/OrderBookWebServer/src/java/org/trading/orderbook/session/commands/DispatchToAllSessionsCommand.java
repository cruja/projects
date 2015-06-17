package org.trading.orderbook.session.commands;

import javax.json.JsonObject;
import org.trading.orderbook.session.SessionManager;

public class DispatchToAllSessionsCommand implements SessionCommand {

    private final SessionManager sessionManager;

    private final JsonObject message;

    public DispatchToAllSessionsCommand(
            SessionManager sessionManager,
            JsonObject message) {
        this.sessionManager = sessionManager;
        this.message = message;
    }

    @Override
    public void process() {
        sessionManager.unsafeDispatchToAllConnectedSessions(message);
    }

}
