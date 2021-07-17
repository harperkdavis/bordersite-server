package engine.map.collision;

import engine.math.Vector3f;

public class Collision {

    private final Vector3f positionResult;
    private final Vector3f velocityResult;
    private final boolean resultGrounded;

    public Collision(Vector3f positionResult, Vector3f velocityResult, boolean resultGrounded) {
        this.positionResult = positionResult;
        this.velocityResult = velocityResult;
        this.resultGrounded = resultGrounded;
    }

    public Vector3f getPositionResult() {
        return positionResult;
    }

    public Vector3f getVelocityResult() {
        return velocityResult;
    }

    public boolean isResultGrounded() {
        return resultGrounded;
    }

}
