package com.gmail.mariodeu2.ffa;

/*
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;


public class main extends JavaPlugin {

    public static itemManager items;

    public static ReentrantLock lock = new ReentrantLock();
    public static HashMap<Player, gameHandler.attackedPlayer> playersAttacked = new HashMap<>();


    public static gameMode currentMode = gameMode.STICK;
    public static Connection connection;

    @Override
    public void onEnable() {

        /*try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://80.101.121.163:3306/players", "lobby", "SchoolIsLeuk");

        } catch (Exception ignored) {
            Bukkit.getLogger().severe("Connection failed");
            Bukkit.shutdown();
        }*/

        config configuration = new config();
        items = new itemManager();
        configuration.setupConfig();
        configuration.loadConfig();

        items.normalStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        items.snowball.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        this.getCommand("ffa").setExecutor(new Commands());
        this.getCommand("ffa").setTabCompleter(new Commands());

        this.getCommand("stats").setExecutor(new Commands());
        this.getCommand("stats").setTabCompleter(new Commands());

        this.getCommand("points").setExecutor(new Commands());
        this.getCommand("points").setTabCompleter(new Commands());


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InvManager(), this);
        getServer().getPluginManager().registerEvents(new gameHandler(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Database.updateAll(player);
        }
        try {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            connection.close();
        } catch (SQLException ignored) {
        }
        // Database.loopTask.cancel();
    }

    public enum gameMode {
        STICK,
        NOSTICKS,
        SNOWBALL,
        SHOOTIE_SHOOT
    }
}
