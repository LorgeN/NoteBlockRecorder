package org.tanberg.noteblockrecorder.util;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class NMSServerUtil {

    /**
     * The "currentTick" field of the NMS MinecraftServer class
     */
    private static Field CURRENT_TICK_FIELD;

    static {
        try {
            Class<?> minecraftServerClass = Class.forName(
                "net.minecraft.server." + getServerVersion() + ".MinecraftServer");
            CURRENT_TICK_FIELD = minecraftServerClass.getDeclaredField("currentTick");
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The current server tick
     */
    public static int getCurrentTick() {
        try {
            return (int) CURRENT_TICK_FIELD.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Returns the version of your server
     *
     * @return The server version
     */
    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
}
