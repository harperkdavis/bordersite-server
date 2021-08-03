package engine.game;

import engine.map.MapLevel;
import engine.map.collision.Collision;
import engine.map.components.Component;
import engine.math.Mathf;
import engine.math.Vector2f;
import engine.math.Vector3f;
import main.Main;
import net.ServerHandler;
import net.ServerTick;
import net.packets.event.HitEvent;
import net.packets.event.RespawnEvent;
import net.packets.event.ShootEvent;

import java.util.Random;

public class PlayerHandler {

    public static final float PLAYER_RADIUS = 0.5f;
    private static final float PLAYER_SPEED = 8.0f;
    private static final float DELTA_TIME = 2.0f / 1000.0f;

    public static void updateMovement(NetPlayer netPlayer, int subtick) {
        if (netPlayer.getPlayer().isDead()) {
            netPlayer.getPlayer().setDeathTimer(netPlayer.getPlayer().getDeathTimer() - 2.0f / 1000.0f);
            if (netPlayer.getPlayer().getDeathTimer() <= 0) {
                respawn(netPlayer.getPlayer(), subtick);
            }
            return;
        }

        Player player = netPlayer.getPlayer();
        player.rotation = netPlayer.getCameraRotation(subtick);

        if (player.grounded) {
            player.velY = 0;
            if (netPlayer.isInputDown("jump", subtick)) {
                player.velY += 0.016f;
                player.grounded = false;
                player.position.add(0, 0.1f, 0);
            }
        } else {
            player.velY -= 0.00008f;
        }

        player.aiming = netPlayer.isInput("aim", subtick);
        player.crouching = netPlayer.isInput("crouch", subtick);
        player.sprinting = netPlayer.isInput("sprint", subtick) && (!player.crouching && !player.aiming);
        player.sprintModifier = Mathf.lerp(player.sprintModifier, player.sprinting ? 1.0f : 0.0f, 0.04f);
        player.crouchModifier = Mathf.lerp(player.crouchModifier, player.crouching ? 1.0f : 0.0f, 0.08f);

        player.velocityForward = Mathf.lerp(player.velocityForward, netPlayer.isInput("move_forward", subtick) ? 1.0f : 0.0f, 0.04f);
        player.velocityBack = Mathf.lerp(player.velocityBack, netPlayer.isInput("move_backward", subtick) ? 1.0f : 0.0f, 0.04f);
        player.velocityLeft = Mathf.lerp(player.velocityLeft, netPlayer.isInput("move_left", subtick) ? 1.0f : 0.0f, 0.04f);
        player.velocityRight = Mathf.lerp(player.velocityRight, netPlayer.isInput("move_right", subtick) ? 1.0f : 0.0f, 0.04f);

        Vector2f inputVector = new Vector2f(player.velocityRight - player.velocityLeft, player.velocityForward - player.velocityBack);
        player.moving = inputVector.magnitude() > 0.5f;
        if (inputVector.magnitude() > 1.0f) {
            inputVector.normalize();
        }
        float rotation = (float) -Math.toRadians(player.rotation.getY());

        Vector3f forward = new Vector3f((float) Math.cos(rotation - Math.PI / 2), 0, (float) Math.sin(rotation - Math.PI / 2));
        Vector3f right = new Vector3f((float) Math.cos(rotation), 0, (float) Math.sin(rotation));

        Vector3f movement = forward.times(inputVector.getY()).plus(right.times(inputVector.getX()));

        float speedMultiplier = PLAYER_SPEED * (player.sprinting ? 1.0f + 0.6f * player.sprintModifier : 1.0f) * (player.crouching ? 1.0f - player.crouchModifier * 0.5f : 1.0f) * (player.aiming ? 0.6f : 1.0f);;

        player.velX += movement.getX() * 0.0001f * speedMultiplier;
        player.velZ += movement.getZ() * 0.0001f * speedMultiplier;

        Vector3f previous = player.position.copy();
        player.position.add(player.velX, player.velY, player.velZ);

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

        player.velX = Mathf.lerp(player.velX, 0, 0.1f);
        player.velZ = Mathf.lerp(player.velZ, 0, 0.1f);

        player.playerHeight = Mathf.lerp(player.playerHeight, 1.5f - player.crouchModifier * 0.5f, 0.02f);

        // GUNPLAY
        if (player.getReloadTime() <= 0) {
            if (netPlayer.isInputDown("reload", subtick) && netPlayer.getPlayer().getAmmo() < 30) {
                player.setReloadTime(150.0f);
            }
        } else if (player.getReloadTime() > 0) {
            player.setReloadTime(player.getReloadTime() - 0.1f);
            if (player.getReloadTime() <= 0) {
                player.setAmmo(30);
            }
        }
    }

    public static void respawn(Player player, int subtick) {
        int team = player.getTeam();
        player.respawn((team == 0 ? MapLevel.getRedSpawn() : MapLevel.getBlueSpawn()).plus(new Random().nextFloat() * 8 - 4, 0, new Random().nextFloat() * 8 - 4), team == 0 ? new Vector3f(0, -90, 0) : new Vector3f(0, 90, 0));
        RespawnEvent respawnEvent = new RespawnEvent(player, subtick);
        ServerHandler.addEvent(respawnEvent);
    }

    public static void shoot(NetPlayer player, int subtick) {
        if (player.getPlayer().isDead()) {
            return;
        }
        float time = (float) ServerTick.getTick() + (float) subtick / 10;
        if (time - player.getPlayer().getLastShot() >= 2.8f) {
            if (player.getPlayer().getAmmo() <= 0 || player.getPlayer().getReloadTime() > 0) {
                return;
            }
            Vector3f start = player.getPlayer().getPosition().plus(0, player.getPlayer().getPlayerHeight(), 0), end = start.plus(player.getForward(subtick).times(100));
            player.getPlayer().setAmmo(player.getPlayer().getAmmo() - 1);
            ServerHandler.addEvent(new ShootEvent(start, subtick));
            for (Player p : ServerHandler.getPlayerList()) {
                if (p.getUuid().equals(player.getUuid()) || (p.isDead() || p.getTeam() == player.getPlayer().getTeam())) {
                    continue;
                }
                int tickDelay = ServerTick.getTick() - (int) player.getPlayer().getPing() / 20;
                float subtickDelay = (player.getPlayer().getPing() % 20) / 20.0f;
                WorldState a = ServerHandler.stateHistory.get(tickDelay - 1), b = ServerHandler.stateHistory.get(tickDelay);
                Vector3f lagPosition = p.getPosition();
                if (!(a == null || b == null)) {
                    Player playerA = null, playerB = null;
                    for (Player ap : a.getPlayers()) {
                        if (ap.getUuid().equals(p.getUuid())) {
                            playerA = ap;
                        }
                    }
                    for (Player bp : b.getPlayers()) {
                        if (bp.getUuid().equals(p.getUuid())) {
                            playerB = bp;
                        }
                    }
                    if (playerA == null || playerB == null) {
                        return;
                    }
                    lagPosition = Vector3f.lerp(playerA.getPosition(), playerB.getPosition(), subtickDelay);
                }

                Vector3f headHitbox = Mathf.rayCylinder(start, end, lagPosition.plus(0, 1.5f, 0), PLAYER_RADIUS * 0.5f, 0.4f);
                Vector3f torsoHitbox = Mathf.rayCylinder(start, end, lagPosition.plus(0, 0.8f, 0), PLAYER_RADIUS * 1.2f, 0.7f);
                Vector3f legsHitbox = Mathf.rayCylinder(start, end, lagPosition, PLAYER_RADIUS, 0.8f);

                Vector3f point = null;
                int hitType = -1;
                float minDistance = Float.MAX_VALUE;
                if (torsoHitbox != null) {
                    point = torsoHitbox.copy();
                    minDistance = Vector3f.distance(start, point);
                    hitType = 0;
                }
                if (legsHitbox != null) {
                    float distance = Vector3f.distance(start, legsHitbox);
                    if (distance < minDistance) {
                        point = legsHitbox.copy();
                        minDistance = distance;
                        hitType = 1;
                    }
                }
                if (headHitbox != null) {
                    float distance = Vector3f.distance(start, headHitbox);
                    if (distance < minDistance) {
                        point = headHitbox.copy();
                        minDistance = distance;
                        hitType = 2;
                    }
                }

                if (point != null) { // HIT!
                    for (Component component : MapLevel.components) {
                        Vector3f raycastPoint = component.getRaycast(start, end);
                        if (raycastPoint != null) {
                            if (Vector3f.distance(start, raycastPoint) < minDistance) {
                                return;
                            }
                        }
                    }

                    HitEvent hitEvent;
                    if (hitType == 0) {
                        hitEvent = new HitEvent(p, player.getPlayer(), 60, minDistance, false, subtick);
                    } else if (hitType == 1) {
                        hitEvent = new HitEvent(p, player.getPlayer(), 40, minDistance, false, subtick);
                    } else {
                        hitEvent = new HitEvent(p, player.getPlayer(), 200, minDistance, true, subtick);
                    }
                    p.damage(hitEvent);
                    ServerHandler.addEvent(hitEvent);
                }
            }
            player.getPlayer().setLastShot(time);
        }
    }

}
