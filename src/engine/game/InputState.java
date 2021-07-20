package engine.game;

import engine.math.Vector3f;

import java.util.ArrayList;

public class InputState {

    private ArrayList<String> keys;
    private Vector3f rotation;

    public InputState(ArrayList<String> keys, Vector3f rotation) {
        this.keys = keys;
        this.rotation = rotation;
    }

    public InputState() {
        this.keys = new ArrayList<>();
        this.rotation = Vector3f.zero();
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
