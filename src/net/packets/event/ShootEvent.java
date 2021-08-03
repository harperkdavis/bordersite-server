package net.packets.event;

import engine.math.Vector3f;

public class ShootEvent implements Event {

    private final Vector3f shootPosition;
    private final int subtick;

    public ShootEvent(Vector3f shootPosition, int subtick) {
        this.shootPosition = shootPosition;
        this.subtick = subtick;
    }

    public ShootEvent() {
        this.shootPosition = null;
        this.subtick = 0;
    }


    public Vector3f getShootPosition() {
        return shootPosition;
    }

    public int getSubtick() {
        return subtick;
    }
}
