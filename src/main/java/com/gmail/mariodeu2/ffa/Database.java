package com.gmail.mariodeu2.ffa;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    // public static BukkitTask loopTask;
    public static HashMap<Player, ArrayList<Object>> stats = new HashMap<>();
    public static Plugin plugin = JavaPlugin.getPlugin(main.class);
    // public static HashMap<Player, Long> timers = new HashMap<>();


    public static int getPlayerKills(Player player) {
        return player.getMetadata("player_kills").get(0).asInt();
    }

    public static int getPlayerKillstreak(Player player) {
        return player.getMetadata("player_killstreak").get(0).asInt();
    }

    public static int getPlayerDeaths(Player player) {
        return player.getMetadata("player_deaths").get(0).asInt();
    }

    public static double getPlayerPoints(Player player) {
        return player.getMetadata("player_points").get(0).asDouble();
    }

    public static void updateAll(Player player) {

        ArrayList<Object> list = stats.get(player);
        stats.remove(player);

        player.setMetadata("player_kills", new StandardMetadataValue(plugin, getPlayerKills(player) + (int) list.get(0)));
        player.setMetadata("player_killstreak", new StandardMetadataValue(plugin, getPlayerKillstreak(player) + (int) list.get(2)));
        player.setMetadata("player_deaths", new StandardMetadataValue(plugin, getPlayerDeaths(player) + (int) list.get(1)));
        player.setMetadata("player_points", new StandardMetadataValue(plugin, getPlayerPoints(player) + (int) list.get(3)));
    }

    public static boolean playerExists(Player player) {
        return player.getMetadata("player_kills").isEmpty();
    }

    public static void createPlayerProfile(Player player) {
        player.setMetadata("player_kills", new StandardMetadataValue(plugin, 0));
        player.setMetadata("player_killstreak", new StandardMetadataValue(plugin, 0));
        player.setMetadata("player_deaths", new StandardMetadataValue(plugin, 0));
        player.setMetadata("player_points", new StandardMetadataValue(plugin, 0D));
    }

    public static class StandardMetadataValue implements MetadataValue{

        Object value;
        Plugin p;

        public StandardMetadataValue(Plugin p, Object o) {
            this.p = p;
            this.value = o;
        }

        @Override
        public boolean equals(Object obj) {
            return value.equals(obj);
        }

        @Override
        public boolean asBoolean() {
            return (boolean) value;
        }

        @Override
        public byte asByte() {
            return (byte) value;
        }

        @Override
        public double asDouble() {
            return (double) value;
        }

        @Override
        public float asFloat() {
            return (float) value;
        }

        @Override
        public int asInt() {
            return (int) value;
        }

        @Override
        public long asLong() {
            return (long) value;
        }

        @Override
        public short asShort() {
            return (short) value;
        }

        @Override
        public @NotNull String asString() {
            return (String) value;
        }

        @Override
        public Plugin getOwningPlugin() {
            return p;
        }

        @Override
        public void invalidate() {
        }

        @Override
        public Object value() {
            return value;
        }
    }
}
