package com.example.exemserver.service;



import com.example.exemserver.dto.AlertResponse;
import com.example.exemserver.dto.CancelResponse;
import com.example.exemserver.service.alert.Alert;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SocketService {
    private final ObjectMapper mapper;

    public void sendAlert(WebSocketSession session, Alert alert, String[] line, int before) {

        AlertResponse response = new AlertResponse(alert, line[0], line[1], before);

        try {
            String json = mapper.writeValueAsString(response);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            session.sendMessage(new TextMessage(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCancelMessage(WebSocketSession session, String[] line, int before){
        CancelResponse response = new CancelResponse(line, before);

        try {
            String json = mapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeSession(WebSocketSession session){
        try {
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
