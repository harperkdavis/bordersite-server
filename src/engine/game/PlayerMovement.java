package engine.game;

import engine.map.MapLevel;
import engine.map.collision.Collision;
import engine.map.components.Component;
import engine.math.Mathf;
import engine.math.Vector3f;
import main.Main;

public class PlayerMovement {

    public static final float PLAYER_RADIUS = 0.5f;
    private static final float DELTA_TIME = 2.0f / 1000.0f;

    public static void updateMovement(NetPlayer netPlayer, int subtick) {
        Player player = netPlayer.getPlayer();
        if (player.grounded) {
            player.velY = 0;
            if (netPlayer.isInputDown("jump", subtick)) {
                player.velY = 10.0f;
                player.position.add(0, 0.1f, 0);
                player.grounded = false;
            }
        } else {
            player.velY -= 0.2f * Main.getDeltaTime() * 120;
        }

        if (netPlayer.isInput("sprint", subtick)) {
            player.sprinting = true;
        }
        player.sprintModifier = Mathf.lerpdt(player.sprintModifier, (player.sprinting ? 1 : 0), 0.1f);

        if (netPlayer.isInputDown("crouch", subtick)) {
            player.crouching = !player.crouching;
        }

        boolean isAiming = netPlayer.isInput("aim", subtick);

        player.velocityLeft = Mathf.lerpdt(player.velocityLeft, netPlayer.isInput("move_left", subtick) ? 1 : 0, 0.5f);
        player.velocityRight = Mathf.lerpdt(player.velocityRight, netPlayer.isInput("move_right", subtick) ? 1 : 0, 0.5f);
        player.velocityForward = Mathf.lerpdt(player.velocityForward, netPlayer.isInput("move_forward", subtick) ? 1 : 0, 0.5f);
        player.velocityBack = Mathf.lerpdt(player.velocityBack, netPlayer.isInput("move_backward", subtick) ? 1 : 0, 0.5f);

        Vector3f inputVector = new Vector3f(player.velocityRight - player.velocityLeft, 0, player.velocityForward - player.velocityBack);

        boolean startedMoving = player.moving;
        player.moving = inputVector.magnitude() > 0.2f && player.grounded;

        startedMoving = !startedMoving && player.moving;

        player.sprinting = player.sprinting && (!player.crouching && !isAiming && player.moving);

        if (inputVector.magnitude() > 1) {
            inputVector.normalize();
        }

        Vector3f forwards = new Vector3f((float) Math.sin(Math.toRadians(netPlayer.getCameraRotation(subtick).getY()) + Math.PI), 0, (float) Math.cos(Math.toRadians(netPlayer.getCameraRotation(subtick).getY()) + Math.PI));
        Vector3f right = new Vector3f((float) Math.sin(Math.toRadians(netPlayer.getCameraRotation(subtick).getY()) + Math.PI / 2), 0, (float) Math.cos(Math.toRadians(netPlayer.getCameraRotation(subtick).getY()) + Math.PI / 2));

        Vector3f movement = Vector3f.add(forwards.multiply(inputVector.getZ()), right.multiply(inputVector.getX()));

        float SPRINT_SPEED = 0.3f;
        float CROUCH_SPEED = 0.4f;
        float MOVE_SPEED = 4.0f;
        float movementSpeed = MOVE_SPEED * (player.crouching ? CROUCH_SPEED : 1.0f) * (1 + SPRINT_SPEED * player.sprintModifier * (isAiming ? 0.8f : 1.0f) * (player.grounded ? 1.0f : 0.01f));
        movement.multiply(movementSpeed);

        Vector3f previous = new Vector3f(player.position);

        if (player.grounded) {
            player.velY = 0;
        }

        player.velX += movement.getX();
        player.velY += movement.getY();
        player.velZ += movement.getZ();

        player.position.add(new Vector3f(player.velX * Main.getDeltaTime(), player.velY * Main.getDeltaTime(), player.velZ * Main.getDeltaTime()));

        boolean hasLanded = player.grounded;

        player.grounded = false;
        for (Component collider : MapLevel.components) {
            Collision result = collider.getCollision(previous, player.position, new Vector3f(player.velX, player.velY, player.velZ), player.crouching ? 1.0f : 1.5f, player.grounded);
            if (result != null) {
                player.position = new Vector3f(result.getPositionResult());

                player.velX = result.getVelocityResult().getX();
                player.velY = result.getVelocityResult().getY();
                player.velZ = result.getVelocityResult().getZ();

                if (result.isResultGrounded()) {
                    player.grounded = true;
                    player.velY = 0;
                }
            }
        }

        hasLanded = !hasLanded && player.grounded;
        if (hasLanded) {

        }

        if (player.grounded) {
            player.velX /= Math.pow(128.0f, Main.getDeltaTime() * 120);
            player.velY /= Math.pow(128.0f, Main.getDeltaTime() * 120);
            player.velZ /= Math.pow(128.0f, Main.getDeltaTime() * 120);
        } else {
            player.velX /= Math.pow(64.0f, Main.getDeltaTime() * 120);
            player.velZ /= Math.pow(64.0f, Main.getDeltaTime() * 120);
        }

        player.playerHeight = Mathf.lerpdt(player.playerHeight, player.crouching ? 1f : 1.5f, 0.01f); //+ bobbing * bobbingMultiplier * (isSprinting ? 2.5f : (isCrouching ? 1.5f : 1.0f)) * 0.01f;

        player.rotation = netPlayer.getCameraRotation(subtick);

    }

}
