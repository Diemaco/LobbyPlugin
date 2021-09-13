package com.gmail.mariodeu2.ffa;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Settings {

    static Plugin plugin = JavaPlugin.getPlugin(main.class);

    public static NamespacedKey NSKVersion = new NamespacedKey(plugin, "FFA-VERSION");
    public static NamespacedKey NSKKillstreak = new NamespacedKey(plugin, "FFA-killstreak");
    public static NamespacedKey NSKKills = new NamespacedKey(plugin, "FFA-kills");
    public static NamespacedKey NSKDeaths = new NamespacedKey(plugin, "FFA-deaths");
    public static NamespacedKey NSKPoints = new NamespacedKey(plugin, "FFA-points");
    public static World world = Bukkit.getWorld("world");
    public static Location lobbySpawnLocation = new Location(world, 27.5, 108, -25, 180, 45);

    //////////////
    // Defaults //
    //////////////
    public static String prefix = "&4&l[&c&lFFA&4&l]&r - ";
    public static Integer damageTimeout = 140;
    public static Double leave_bonus = 0.25;
    public static String player_fall = "&aYou died!";
    public static String player_lose = "&a&l{player}&r&a knocked you into lava!";
    public static String player_win = "&aYou knocked &l{player}&r&a into lava!";
    public static String player_leave = "{prefix}&a&l{player}&r&a left the game so you received {leave_bonus} points";
    public static String command_wrong = "{prefix}&cWrong usage!";
    public static String stat = "&4&lFFA-{stat}:{newline}&r";

    public Settings() {

    }

    public void setupConfig() {
        FileConfiguration config = plugin.getConfig();


        config.addDefault("damageTimeout", damageTimeout);
        config.addDefault("leaveBonus", leave_bonus);

        config.addDefault("prefix", prefix);
        config.addDefault("playerFallMessage", player_fall);
        config.addDefault("playerLoseMessage", player_lose);
        config.addDefault("playerWinMessage", player_win);
        config.addDefault("playerLeaveMessage", player_leave);

        config.addDefault("commandWrong", command_wrong);
        config.addDefault("stat", stat);

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        damageTimeout =  config.getInt("damageTimeout");
        leave_bonus = config.getDouble("leaveBonus");

        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));
        player_fall = ChatColor.translateAlternateColorCodes('&', config.getString("playerFallMessage"));
        player_lose = ChatColor.translateAlternateColorCodes('&', config.getString("playerLoseMessage"));
        player_win = ChatColor.translateAlternateColorCodes('&', config.getString("playerWinMessage"));
        player_leave = ChatColor.translateAlternateColorCodes('&', config.getString("playerLeaveMessage"));

        command_wrong = ChatColor.translateAlternateColorCodes('&', config.getString("commandWrong"));
        stat = ChatColor.translateAlternateColorCodes('&', config.getString("stat"));
    }
}