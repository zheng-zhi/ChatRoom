package me.zzheng.projects.webscoket.infrastructure;

import me.zzheng.projects.webscoket.domain.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.time.LocalTime;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public Message decode(final String textMessage) throws DecodeException {
        Message message = new Message();
        JsonReader jsonReader = Json.createReader(new StringReader(textMessage));
        JsonObject jsonObject = jsonReader.readObject();
        message.setContent(jsonObject.getString("content"));
        message.setSender(jsonObject.getString("sender"));
        message.setReceived(LocalTime.now().toString());
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
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
