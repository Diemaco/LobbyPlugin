package com.gmail.mariodeu2.ffa;

import com.gmail.mariodeu2.ffa.commands.FFACommand;
import com.gmail.mariodeu2.ffa.commands.PointsCommand;
import com.gmail.mariodeu2.ffa.commands.StatsCommand;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;


public class main extends JavaPlugin {

    public static Items items;

    public static final ReentrantLock lock = new ReentrantLock();
    public static final HashMap<Player, GameEvents.attackedPlayer> playersAttacked = new HashMap<>();


    public static gameMode currentMode = gameMode.STICK;

    @Override
    public void onEnable() {

        Settings configuration = new Settings();
        items = new Items();
        configuration.setupConfig();
        configuration.loadConfig();

        items.normalStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        items.snowball.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        CommandManager commandManager = new CommandManager();

        commandManager.registerCommand(new StatsCommand());
        commandManager.registerCommand(new PointsCommand());
        commandManager.registerCommand(new FFACommand());


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InvEvents(), this);
        getServer().getPluginManager().registerEvents(new GameEvents(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Database.updateAllAndRemoveFromCache(player);
        }
    }

    public enum gameMode {
        STICK,
        NOSTICKS,
        SNOWBALL,
        SHOOTIE_SHOOT
    }
}
