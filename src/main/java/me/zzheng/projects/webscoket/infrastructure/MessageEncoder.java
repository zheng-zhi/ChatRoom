package me.zzheng.projects.webscoket.infrastructure;

import me.zzheng.projects.webscoket.domain.Message;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(Message message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("content", message.getContent())
                .add("sender", message.getSender())
                .add("received", message.getReceived())
                .build()
                .toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Not implemented
    }

    @Override
    public void destroy() {
        // Not implemented
    }
}
