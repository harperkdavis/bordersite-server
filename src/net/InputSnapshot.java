package net;

import java.util.ArrayList;

public class InputSnapshot {

    private final long timestamp;
    private final ArrayList<String> inputs;

    public InputSnapshot(ArrayList<String> inputs) {
        this.inputs = inputs;
        timestamp = System.currentTimeMillis();
    }

    public InputSnapshot() {
        this.inputs = null;
        this.timestamp = 0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "{timestamp: " + timestamp + ", " + inputs + "}";
    }
}
