package net.packets.event;

import engine.game.Player;

public class RespawnEvent implements Event {

    private final Player respawned;
    private final int subtick;

    public RespawnEvent(Player respawned, int subtick) {
        this.respawned = respawned.copy();
        this.subtick = subtick;
    }

    public Player getRespawned() {
        return respawned;
    }

    public int getSubtick() {
        return subtick;
    }

}
