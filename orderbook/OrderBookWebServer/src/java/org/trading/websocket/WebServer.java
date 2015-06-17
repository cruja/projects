package org.trading.websocket;

import javax.annotation.PostConstruct;
import org.trading.orderbook.session.SessionManager;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.trading.orderbook.connectors.upstream.UpstreamMessageProcessor;
import org.trading.orderbook.dispatch.DispatcherManager;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebServer {

    @Inject
    private SessionManager sessionManager;
    
    @Inject 
    private DispatcherManager dispatcherManager;

    @Inject
    private UpstreamMessageProcessor messageProcessor;

    @PostConstruct
    public void onPostConstruct(){
        dispatcherManager.started();
    }
    
    @OnOpen
    public void open(Session session) {
        sessionManager.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionManager.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        messageProcessor.processMessage(message);
    }

}
