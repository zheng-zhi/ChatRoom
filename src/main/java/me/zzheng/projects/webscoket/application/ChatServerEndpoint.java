package me.zzheng.projects.webscoket.application;

import me.zzheng.projects.webscoket.domain.Message;
import me.zzheng.projects.webscoket.domain.Room;
import me.zzheng.projects.webscoket.infrastructure.MessageDecoder;
import me.zzheng.projects.webscoket.infrastructure.MessageEncoder;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static me.zzheng.projects.webscoket.utils.Messages.WELCOME_MESSAGE;
import static me.zzheng.projects.webscoket.utils.Messages.objectify;

@ServerEndpoint(value = "/chat/{roomName}/{userName}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatServerEndpoint {

    private final Logger log = Logger.getLogger(ChatServerEndpoint.class.getSimpleName());

    private static final Map<String, Room> rooms = Collections.synchronizedMap(new HashMap<String, Room>());
    private static final String[] roomNames = {"Room1", "Room2", "Room3", "Room4"};

    @PostConstruct
    public void initialise() {
        Arrays.stream(roomNames).forEach(roomName -> rooms.computeIfAbsent(roomName, new Room(roomName)));
    }

    @OnOpen
    public void onOpen(final Session session,
                       @PathParam("roomName") String roomName,
                       @PathParam("userName") String userName) throws IOException, EncodeException {

        // Set session level configurations
        session.getUserProperties().putIfAbsent("roomName", roomName);
        session.getUserProperties().putIfAbsent("userName", userName);

        // Time out after 5 minutes
        session.setMaxIdleTimeout(5 * 60 * 1000);

        // Store session in room
        Room room = rooms.get(roomName);
        room.join(session);

        // Send welcome message
        session.getBasicRemote().sendObject(objectify(WELCOME_MESSAGE));
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        rooms.get(extractRoomFrom(session)).sendMessage(message);
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message, Session session) {
        // Not implemented
    }

    @OnMessage
    public void onPongMessage(PongMessage message, Session session) {
        // Not implemented
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info(reason::getReasonPhrase);
        rooms.get(extractRoomFrom(session)).leave(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info(error::getMessage);
    }

    /**
     * Extracts the room from the session
     *
     * @param session the session object
     * @return the room name
     */
    private String extractRoomFrom(Session session) {
        return (String) session.getUserProperties().get("roomName");
    }

    /**
     * Returns the list of rooms in chat application
     * @return Map of room names to room instances
     */
    static Map<String, Room> getRooms() {
        return rooms;
    }
}
