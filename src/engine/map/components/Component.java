package engine.map.components;

import engine.map.collision.Collision;
import engine.math.Vector3f;

public interface Component {

    Collision getCollision(Vector3f previous, Vector3f position, Vector3f velocity, float height, boolean isGrounded);
    boolean isGrounded(Vector3f position);

}
