package net;

import java.util.TimerTask;

public class ServerTick extends TimerTask {

    private static int tick = 0;
    private static int subtick = 0;

    @Override
    public void run() {
        ServerHandler.executeSubtick(subtick);
        subtick++;
        if (subtick >= 10) {
            ServerHandler.executeTick(tick);
            tick++;
            subtick = 0;
        }
    }

    public static int getTick() {
        return tick;
    }

    public static int getSubtick() {
        return subtick;
    }
}
