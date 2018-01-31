package me.zzheng.projects.webscoket.application;

import me.zzheng.projects.webscoket.utils.Messages;

import javax.ejb.Stateless;

@Stateless
public class RobotSchedular {

    private void interrupt() {
        ChatServerEndpoint.getRooms().forEach((s, room) -> room.sendMessage(Messages.objectify("Hello from Robot!")));
    }
}
