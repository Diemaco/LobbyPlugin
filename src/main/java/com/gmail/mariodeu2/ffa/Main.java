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


public class Main extends JavaPlugin {

    public static ItemCreator itemCreator;

    public static final ReentrantLock lock = new ReentrantLock();
    public static final HashMap<Player, GameEvents.attackedPlayer> playersAttacked = new HashMap<>();


    public static gameMode currentMode = gameMode.STICK;

    @Override
    public void onEnable() {

        Settings configuration = new Settings();
        itemCreator = new ItemCreator();
        configuration.setupConfig();
        configuration.loadConfig();

        itemCreator.normalStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        itemCreator.snowball.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        CommandManager commandManager = new CommandManager();

        commandManager.registerCommand(new StatsCommand());
        commandManager.registerCommand(new PointsCommand());
        commandManager.registerCommand(new FFACommand());


        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getServer().getPluginManager().registerEvents(new GameEvents(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataStorage.saveAndClear(player);
        }
    }

    public enum gameMode {
        STICK,
        NOSTICKS,
        SNOWBALL,
        SHOOTIE_SHOOT
    }
}
