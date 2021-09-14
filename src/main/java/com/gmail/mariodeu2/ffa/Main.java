package com.gmail.mariodeu2.ffa;

import com.gmail.mariodeu2.ffa.commands.FFACommand;
import com.gmail.mariodeu2.ffa.commands.PointsCommand;
import com.gmail.mariodeu2.ffa.commands.StatsCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.locks.ReentrantLock;


public class Main extends JavaPlugin {

    public static final ReentrantLock lock = new ReentrantLock();

    public static final ItemCreator itemCreator = new ItemCreator();
    private static final CommandManager commandManager = new CommandManager();

    @Override
    public void onEnable() {
        Settings.setupConfig();
        Settings.loadConfig();

        commandManager.registerCommand(new StatsCommand());
        commandManager.registerCommand(new PointsCommand());
        commandManager.registerCommand(new FFACommand());

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getServer().getPluginManager().registerEvents(new GameEvents(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : PlayerDataStorage.cachedPlayerData.keySet()) {
            PlayerDataStorage.saveAndClear(player);
        }
    }
}
