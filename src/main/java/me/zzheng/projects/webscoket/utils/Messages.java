package me.zzheng.projects.webscoket.utils;

import me.zzheng.projects.webscoket.domain.Message;

import java.time.LocalTime;

import static java.lang.String.format;

public class Messages {

    public static final String WELCOME_MESSAGE = "Welcome to Java Chat";
    public static final String ANNOUNCE_NEW_USER = "%s just entered the room.";
    public static final String ANNOUNCE_LEAVER = "%s just leave the room. We'll miss you~";

    public static String personlize(String message, String... args) {
        return format(message, args);
    }

    public static Message objectify(String content, String... args) {
        return objectify(content, "Robot", LocalTime.now().toString(), args);
    }

    public static Message objectify(String content, String sender, String... args) {
        return objectify(content, sender, LocalTime.now().toString(), args);
    }

    public static Message objectify(String content, String sender, String received, String... args) {
        return new Message(personlize(content, args), sender, received);
    }
}
