package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static com.gmail.mariodeu2.ffa.main.connection;

public class Database {

    public static BukkitTask loopTask;
    public static HashMap<Player, ArrayList<Object>> stats = new HashMap<>();
    public static Plugin plugin = JavaPlugin.getPlugin(main.class);
    // public static HashMap<Player, Long> timers = new HashMap<>();


    public static int getPlayerKills(Player player) {

        return player.getMetadata("player_kills").get(0).asInt();

        /*Statement stmt;
        try {
            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                return rs.getInt("kills");
            }
        } catch (Exception ignored) {
        }

        return 0;*/
    }

    public static int getPlayerKillstreak(Player player) {
        return player.getMetadata("player_killstreak").get(0).asInt();
        /*Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                return rs.getInt("killstreak");
            }
        } catch (Exception ignored) {
        }

        return 0;*/
    }

    public static int getPlayerDeaths(Player player) {
        return player.getMetadata("player_deaths").get(0).asInt();
        /*Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                return rs.getInt("deaths");
            }
        } catch (Exception ignored) {
        }

        return 0;*/
    }

    public static double getPlayerPoints(Player player) {
        return player.getMetadata("player_points").get(0).asDouble();
        /*Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                return rs.getDouble("points");
            }
        } catch (Exception ignored) {
        }

        return 0;*/
    }

    /*public static LocalDateTime getLastTimePlayed(Player player) {

        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                return LocalDateTime.parse(rs.getString("lastPlayed"));
            }
        } catch (Exception ignored) {
        }

        return null;
    }*/

    /*public static LocalDateTime getFirstTimePlayed(Player player) {

        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                return LocalDateTime.parse(rs.getString("firstPlayed"));
            }
        } catch (Exception ignored) {
        }

        return null;
    }*/



    /*
    public static void updatePlayerKills(Player player, int newValue) {

        Statement stmt;
        try {
            stmt = connection.createStatement();
            int rs=stmt.executeUpdate(String.format("UPDATE `lobbyplayerdata` SET `kills`=%d WHERE uuid='%s'", getPlayerKills(player)+newValue, player.getUniqueId()));
        } catch (Exception ignored) { }

    }

    public static void updatePlayerKillstreak(Player player, int newValue) {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            int rs=stmt.executeUpdate(String.format("UPDATE `lobbyplayerdata` SET `killstreak`=%d WHERE uuid='%s'", getPlayerKillstreak(player)+newValue, player.getUniqueId()));
        } catch (Exception ignored) { }
    }

    public static void updatePlayerDeaths(Player player, int newValue) {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            int rs=stmt.executeUpdate(String.format("UPDATE `lobbyplayerdata` SET `deaths`=%d WHERE uuid='%s'", getPlayerDeaths(player)+newValue, player.getUniqueId()));
        } catch (Exception ignored) { }
    }

    public static void updatePlayerPoints(Player player, double newValue) {
        Statement stmt;
        try {
            stmt = connection.createStatement();
            int rs=stmt.executeUpdate(String.format("UPDATE `lobbyplayerdata` SET `points`=%b WHERE uuid='%s'", getPlayerPoints(player)+newValue, player.getUniqueId()));
        } catch (Exception ignored) { }
    }


    */

    public static void updateAll(Player player) {

        ArrayList<Object> list = stats.get(player);
        stats.remove(player);

        player.setMetadata("player_kills", new FixedMetadataValue(main.getPlugin(main.class), getPlayerKills(player) + (int) list.get(0)));
        player.setMetadata("player_killstreak", new FixedMetadataValue(main.getPlugin(main.class), getPlayerKillstreak(player) + (int) list.get(2)));
        player.setMetadata("player_deaths", new FixedMetadataValue(main.getPlugin(main.class), getPlayerDeaths(player) + (int) list.get(1)));
        player.setMetadata("player_points", new FixedMetadataValue(main.getPlugin(main.class), getPlayerPoints(player) + (int) list.get(3)));

        /*try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://80.101.121.163:3306/players", "lobby", "SchoolIsLeuk");
        } catch(Exception ignored) { }

        Statement stmt;
        try {
            stmt = connection.createStatement();


            LocalDateTime time = LocalDateTime.now();




            int rs = stmt.executeUpdate(String.format("UPDATE `lobbyplayerdata` SET `kills`=%d, `killstreak`=%d, `deaths`=%d, `points`=%b WHERE uuid='%s'",
                    getPlayerKills(player) + (int) list.get(0),        // Kills
                    getPlayerKillstreak(player) + (int) list.get(2),   // Killstreak
                    getPlayerDeaths(player) + (int) list.get(1),       // Deaths
                    getPlayerPoints(player) + (double) list.get(3),    // Points
                    player.getUniqueId()));                            // uuid

        } catch (Exception ignored) { }*/
    }

    public static void resetPlayer(Player player) {
        resetPlayer(player, true);
    }


    public static void resetPlayer(Player player, boolean resetDates) {
    }

    public static boolean playerExists(Player player) {

        return player.getMetadata("player_kills").isEmpty();

        /*Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM `lobbyplayerdata` WHERE `uuid`='%s'", player.getUniqueId()));

            if (rs.first()) {
                Bukkit.getLogger().severe("Player exists");
                return true;
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.toString());
        }
        Bukkit.getLogger().info("Player doesn't exist");
        return false;*/
    }

    public static void createPlayerProfile(Player player) {

        player.setMetadata("player_kills", new FixedMetadataValue(main.getPlugin(main.class), 0));
        player.setMetadata("player_killstreak", new FixedMetadataValue(main.getPlugin(main.class), 0));
        player.setMetadata("player_deaths", new FixedMetadataValue(main.getPlugin(main.class), 0));
        player.setMetadata("player_points", new FixedMetadataValue(main.getPlugin(main.class), 0D));


        /*Statement stmt;
        try {
            stmt = connection.createStatement();
            boolean rs = stmt.execute(String.format("INSERT INTO `lobbyplayerdata`(`username`, `uuid`, `timeOnline`, `kills`, `killstreak`, `deaths`, `points`) VALUES ('%s','%s',0,0,0,0,0)", player.getName(), player.getUniqueId()));
            if (!rs) {
                Bukkit.getLogger().info("Created player profile");
            } else {
                Bukkit.getLogger().severe("Internal error with DB");
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.toString());
        }*/
    }
}
