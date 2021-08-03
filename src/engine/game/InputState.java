package engine.game;

import engine.math.Vector3f;

import java.util.ArrayList;

public class InputState {

    private ArrayList<String> keys;
    private Vector3f rotation, forward;

    public InputState(ArrayList<String> keys, Vector3f rotation, Vector3f forward) {
        this.keys = keys;
        this.rotation = rotation;
        this.forward = forward;
    }

    public InputState() {
        this.keys = new ArrayList<>();
        this.rotation = Vector3f.zero();
        this.forward = Vector3f.zero();
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getForward() {
        return forward;
    }
}
