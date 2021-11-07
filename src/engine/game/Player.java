package engine.game;

import engine.math.Vector3f;
import net.ServerHandler;
import net.packets.event.DeathEvent;
import net.packets.event.HitEvent;

import java.util.UUID;

public class Player {

    private int playerId;
    private String uuid;

    private String username;

    private float ping = 0;

    private int team = 0;
    private int kills = 0;
    private boolean registered = false;
    private int inputSequence = 0;

    private int forceMoveTicks = 0;

    // MOVEMENT
    protected Vector3f position = Vector3f.oneY();
    protected Vector3f rotation = Vector3f.zero();

    protected boolean grounded, crouching, sprinting, moving, aiming;
    protected float velX, velY, velZ;
    protected float velocityLeft, velocityRight, velocityForward, velocityBack;
    protected float sprintModifier, crouchModifier;
    protected float playerHeight;

    // GUNPLAY

    private float health = 200;
    private int ammo = 30;
    private float reloadTime = 0, lastShot = 0;
    private boolean dead = false;
    private float deathTimer = 0;
    private Player killer = null;

    public Player(int playerId, String uuid, String username) {
        this.playerId = playerId;
        this.uuid = uuid;
        this.username = username;
    }

    public Player() {
        this.playerId = 0;
        this.uuid = "";
        this.username = "";
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public int getInputSequence() {
        return inputSequence;
    }

    public void setInputSequence(int inputSequence) {
        this.inputSequence = inputSequence;
    }

    public Vector3f getPosition() {
        return position;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public boolean isAiming() {
        return aiming;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public float getVelZ() {
        return velZ;
    }

    public float getSprintModifier() {
        return sprintModifier;
    }

    public float getCrouchModifier() {
        return crouchModifier;
    }

    public boolean isMoving() {
        return moving;
    }

    public float getVelocityLeft() {
        return velocityLeft;
    }

    public float getVelocityRight() {
        return velocityRight;
    }

    public float getVelocityForward() {
        return velocityForward;
    }

    public float getVelocityBack() {
        return velocityBack;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getPlayerHeight() {
        return playerHeight;
    }

    public float getHealth() {
        return health;
    }

    public int getAmmo() {
        return ammo;
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public boolean isDead() {
        return dead;
    }

    public float getLastShot() {
        return lastShot;
    }

    public float getPing() {
        return ping;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setReloadTime(float reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void setLastShot(float lastShot) {
        this.lastShot = lastShot;
    }

    public void setPing(float ping) {
        this.ping = ping;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public float getDeathTimer() {
        return deathTimer;
    }

    public void setDeathTimer(float deathTimer) {
        this.deathTimer = deathTimer;
    }

    public boolean isForceMove() {
        return forceMoveTicks > 0;
    }

    public void setForceMoveTicks(int forceMove) {
        this.forceMoveTicks = forceMove;
    }

    public void decreaseForceMoveTicks() {
        this.forceMoveTicks--;
    }

    public Player getKiller() {
        return killer;
    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        this.kills++;
    }

    public void teleport(Vector3f position) {
        this.position = position;
        this.rotation = Vector3f.zero();
        this.forceMoveTicks = 3;
        this.velX = 0;
        this.velY = 0;
        this.velZ = 0;
    }

    public void teleport(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.forceMoveTicks = 3;
        this.velX = 0;
        this.velY = 0;
        this.velZ = 0;
    }

    public void damage(HitEvent hitEvent) {
        if (this.isDead()) {
            return;
        }
        this.health -= hitEvent.getDamage();
        if (this.health <= 0) {
            kill(hitEvent);
        }
    }

    public void kill(HitEvent hitEvent) {
        deathTimer = 10.0f;
        dead = true;
        health = 0;
        this.killer = hitEvent.getSource().copy();
        ServerHandler.getPlayer(hitEvent.getSource().getPlayerId()).getPlayer().addKill();

        int scoreAdd = 100 + (hitEvent.isHeadshot() ? 50 : 0) + (hitEvent.getDistance() > 40 ? 50 : 0);
        String deathMessage = username + " was " + (hitEvent.isHeadshot() ? "headshot" : "killed") + " by " + hitEvent.getSource().getUsername() + " (" + Math.round(hitEvent.getDistance() * 10) / 10.0f + "m) +" + scoreAdd + " score";

        if (hitEvent.getSource().getTeam() == 0) {
            GameHandler.redScore += scoreAdd;
            ServerHandler.broadcastMessage(deathMessage, ServerHandler.DARK_RED_COLOR);
        } else {
            GameHandler.blueScore += scoreAdd;
            ServerHandler.broadcastMessage(deathMessage, ServerHandler.DARK_BLUE_COLOR);
        }
        DeathEvent deathEvent = new DeathEvent(this, hitEvent.getSource(), hitEvent.getDistance(), hitEvent.isHeadshot(), hitEvent.getSubtick());
        ServerHandler.addEvent(deathEvent);
    }

    public void respawn(Vector3f position, Vector3f rotation) {
        teleport(position, rotation);
        this.dead = false;
        this.health = 200;
        this.deathTimer = -1;
        this.ammo = 30;
        this.killer = null;
    }

    public Player copy() {
        Player copy = new Player(playerId, username, uuid);
        copy.playerId = playerId;
        copy.username = username;

        copy.team = team;
        copy.registered = registered;
        copy.inputSequence = inputSequence;

        copy.forceMoveTicks = forceMoveTicks;

        copy.position = position.copy();
        copy.rotation = rotation.copy();

        copy.grounded = grounded;
        copy.crouching = crouching;
        copy.sprinting = sprinting;
        copy.moving = moving;
        copy.aiming = aiming;

        copy.velX = velX;
        copy.velY = velY;
        copy.velZ = velZ;

        copy.velocityLeft = velocityLeft;
        copy.velocityRight = velocityRight;
        copy.velocityForward = velocityForward;
        copy.velocityBack = velocityBack;

        copy.sprintModifier = sprintModifier;
        copy.crouchModifier = crouchModifier;
        copy.rotation = rotation;
        copy.playerHeight = playerHeight;

        copy.health = health;
        copy.ammo = ammo;
        copy.dead = dead;
        copy.kills = kills;

        copy.deathTimer = deathTimer;
        copy.reloadTime = reloadTime;
        copy.lastShot = lastShot;
        copy.ping = ping;
        copy.killer = killer;

        return copy;
    }

}
