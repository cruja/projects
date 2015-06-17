
package org.trading.orderbook.session.commands;

import javax.websocket.Session;
import org.trading.orderbook.session.SessionManager;


public class RemoveSessionCommand implements SessionCommand{

    private final SessionManager sessionManager;

    private final Session session;

    public RemoveSessionCommand(SessionManager sessionManager, Session session) {
        this.sessionManager = sessionManager;
        this.session = session;
    }

    @Override
    public void process() {
        sessionManager.unsafeRemove(session);
    }
    
    
    
}
