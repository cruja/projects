package org.trading.orderbook.session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.JsonObject;
import javax.websocket.Session;
import org.trading.orderbook.session.commands.AddSessionCommand;
import org.trading.orderbook.session.commands.DispatchToAllSessionsCommand;
import org.trading.orderbook.session.commands.RemoveSessionCommand;
import org.trading.orderbook.session.processor.SessionCommandProcessor;

@Singleton
public class SessionManager {

    @Inject
    private SessionCommandProcessor sessionCommandProcessor;

    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessionCommandProcessor.process(
                new AddSessionCommand(this, session));        
    }

    public void removeSession(Session session) {
        sessionCommandProcessor.process(
                new RemoveSessionCommand(this, session));          
    }

    public void dispatchToAllConnectedSessions(JsonObject message) {
        sessionCommandProcessor.process(
                new DispatchToAllSessionsCommand(this, message));        
    }

    public void unsafeAdd(Session session) {
        sessions.add(session);
    }
    
    public void unsafeRemove(Session session) {
        sessions.remove(session);
    }
    
    public void unsafeDispatchToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            dispatchToSession(session, message);
        }
    }

    private void dispatchToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
        }
    }
}
