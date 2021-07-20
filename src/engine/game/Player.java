package engine.game;

import engine.math.Vector3f;

import java.util.UUID;

public class Player {

    private int playerId;
    private String uuid;

    private String username;

    private int team = 0;
    private boolean registered = false;
    private int inputSequence = 0;

    // MOVEMENT
    protected Vector3f position = new Vector3f(0, 1.5f, 0);
    protected Vector3f rotation = Vector3f.zero();

    protected boolean grounded = false, crouching = false, sprinting = false, moving = false;
    protected float velX = 0, velY = 0, velZ = 0;
    protected float velocityLeft = 0, velocityRight = 0, velocityForward = 0, velocityBack = 0;
    protected float sprintModifier = 0;
    protected float playerHeight = 1.5f;

    public Player(int playerId, String username) {
        this.playerId = playerId;
        this.uuid = UUID.randomUUID().toString();
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

    public Vector3f getRotation() {
        return rotation;
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

    public float getPlayerHeight() {
        return playerHeight;
    }

    public void printData() {
        System.out.println("Player Info Dump");
        System.out.println("Id: " + playerId);
        System.out.println("UUID: " + uuid);
        System.out.println("Username: " + username);

        System.out.println("Team: " + team);
        System.out.println("Registered: " + registered);

        System.out.println("Grounded: " + grounded);
        System.out.println("Crouching: " + crouching);
        System.out.println("Sprinting: " + sprinting);
        System.out.println("Moving: " + moving);

        System.out.println("VelX: " + velX);
        System.out.println("VelY: " + velY);
        System.out.println("VelZ: " + velZ);

        System.out.println("VelLeft: " + velocityLeft);
        System.out.println("VelRight: " + velocityRight);
        System.out.println("VelForward: " + velocityForward);
        System.out.println("VelBack: " + velocityBack);

        System.out.println("SprintModifier: " + sprintModifier);
        System.out.println("Height: " + playerHeight);

        System.out.println("Rotation: " + rotation);
        System.out.println("Position: " + position);
    }

    public Player copy() {
        Player copy = new Player(playerId, username);
        copy.playerId = playerId;
        copy.uuid = "" + uuid;
        copy.username = "" + username;

        copy.team = team;
        copy.registered = registered;

        copy.grounded = grounded;
        copy.crouching = crouching;
        copy.sprinting = sprinting;
        copy.moving = moving;

        copy.velX = velX;
        copy.velY = velY;
        copy.velZ = velZ;

        copy.velocityLeft = velocityLeft;
        copy.velocityRight = velocityRight;
        copy.velocityForward = velocityForward;
        copy.velocityBack = velocityBack;

        copy.sprintModifier = sprintModifier;
        copy.playerHeight = playerHeight;

        copy.rotation = rotation.copy();
        copy.position = position.copy();

        return copy;
    }

}
