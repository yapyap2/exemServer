package com.example.exemserver.config.socket;

import com.example.exemserver.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class AlertSocketHandler extends TextWebSocketHandler {

    private final AlertService alertService;

    HashMap<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);

        System.out.println("new session " + session.getId() + " established.");

        alertService.startAlert(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session.getId() + " session closed.");
        sessions.remove(session.getId());
    }
}
