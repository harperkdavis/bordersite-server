package net.packets.event;

import engine.game.Player;

public class HitEvent implements Event {

    private final Player hit, source;
    private final float damage, distance;
    private final boolean headshot;
    private final int subtick;

    public HitEvent(Player hit, Player source, float damage, float distance, boolean headshot, int subtick) {
        this.hit = hit.copy();
        this.source = source.copy();
        this.damage = damage;
        this.distance = distance;
        this.headshot = headshot;
        this.subtick = subtick;
    }

    public HitEvent() {
        this.hit = null;
        this.source = null;
        this.damage = 0;
        this.distance = 0;
        this.headshot = false;
        this.subtick = 0;
    }

    public Player getHit() {
        return hit;
    }

    public Player getSource() {
        return source;
    }

    public float getDamage() {
        return damage;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isHeadshot() {
        return headshot;
    }

    public int getSubtick() {
        return subtick;
    }
}
