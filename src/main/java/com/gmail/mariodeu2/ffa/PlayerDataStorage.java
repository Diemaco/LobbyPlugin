package com.gmail.mariodeu2.ffa;

import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;

public class PlayerDataStorage {

    public static final HashMap<Player, PlayerData> cachedPlayerData = new HashMap<>();

    public static int getPlayerKills(Player player) {
        return PlayerDataStorage.cachedPlayerData.get(player).kills;
    }

    public static int getPlayerKillstreak(Player player) {
        return PlayerDataStorage.cachedPlayerData.get(player).killstreak;
    }

    public static int getPlayerDeaths(Player player) {
        return PlayerDataStorage.cachedPlayerData.get(player).deaths;
    }

    public static double getPlayerPoints(Player player) {
        return PlayerDataStorage.cachedPlayerData.get(player).points;
    }

    public static void saveAndClear(Player player) {
        cachedPlayerData.get(player).serialize(player);
        cachedPlayerData.remove(player);
    }

    public static boolean playerExists(Player player) {
        return PlayerData.exists(player);
    }

    public static void createPlayerProfile(Player player) {
        new PlayerData(0, 0, 0, 0.0D).serialize(player);
    }

    public static class PlayerData implements Serializable {
        public int kills;
        public int killstreak;
        public int deaths;
        public double points;

        public PlayerData(int kills, int killstreak, int deaths, double points) {
            this.kills = kills;
            this.killstreak = killstreak;
            this.deaths = deaths;
            this.points = points;
        }

        public void serialize(Player player) {
            FileOutputStream fout;
            ObjectOutputStream oos;
            try {
                String path = "/usr/home/diemaco/Minecraft/lobby/playerData/" + player.getUniqueId() + ".txt";

                File f = new File(path);
                f.createNewFile();

                fout = new FileOutputStream(path);
                oos = new ObjectOutputStream(fout);
                oos.writeObject(this);

                fout.close();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static PlayerData deserialize(Player player) {
            ObjectInputStream objectinputstream;
            try {
                FileInputStream streamIn = new FileInputStream("/usr/home/diemaco/Minecraft/lobby/playerData/" + player.getUniqueId() + ".txt");
                objectinputstream = new ObjectInputStream(streamIn);

                return  (PlayerData) objectinputstream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new PlayerData(0, 0, 0,  0.0D);
        }

        public static boolean exists(Player player) {
            return new File("/usr/home/diemaco/Minecraft/lobby/playerData/" + player.getUniqueId() + ".txt").exists();
        }
    }
}
