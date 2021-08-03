package net.packets.event;

import engine.game.Player;

public class DeathEvent implements Event {

    private final Player dead, source;
    private final boolean headshot;
    private final float distance;
    private final int subtick;

    public DeathEvent(Player dead, Player source, float distance, boolean headshot, int subtick) {
        this.dead = dead.copy();
        this.source = source.copy();
        this.distance = distance;
        this.headshot = headshot;
        this.subtick = subtick;
    }

    public DeathEvent() {
        this.dead = null;
        this.source = null;
        this.distance = 0;
        this.headshot = false;
        this.subtick = 0;
    }

    public Player getDead() {
        return dead;
    }

    public Player getSource() {
        return source;
    }

    public boolean isHeadshot() {
        return headshot;
    }

    public float getDistance() {
        return distance;
    }

    public int getSubtick() {
        return subtick;
    }

}
