package net;

import engine.math.Vector3f;

import java.util.ArrayList;

public class InputSnapshot {

    private final int timestamp;
    private final ArrayList<String> inputs;
    private final Vector3f rotation, forward;

    public InputSnapshot(ArrayList<String> inputs, Vector3f rotation, Vector3f forward, int timestamp) {
        this.inputs = inputs;
        this.rotation = rotation;
        this.forward = forward;
        this.timestamp = timestamp;
    }

    public InputSnapshot() {
        this.inputs = null;
        this.rotation = Vector3f.zero();
        this.forward = Vector3f.zero();
        this.timestamp = 0;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getInputs() {
        return inputs;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getForward() {
        return forward;
    }

    @Override
    public String toString() {
        return "{timestamp: " + timestamp + ", " + inputs + ", rotation: " + rotation + "}";
    }
}
