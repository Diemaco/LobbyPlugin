package com.gmail.mariodeu2.ffa;

import com.gmail.mariodeu2.ffa.commands.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final List<CommandInterface> registeredCommands = new ArrayList<>();

    public void registerCommand(CommandInterface command) {
        JavaPlugin plugin = JavaPlugin.getPlugin(Main.class);

        if (!registeredCommands.contains(command)) {
            PluginCommand pluginCommand = plugin.getCommand(command.getName());

            if (pluginCommand != null) {
                pluginCommand.setExecutor(this);
                pluginCommand.setTabCompleter(this);
                pluginCommand.setUsage(command.getUsage());
                pluginCommand.setDescription(command.getDescription());

                registeredCommands.add(command);
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        for (CommandInterface registeredCommand : registeredCommands) {
            if (registeredCommand.getName().equals(command.getName().toLowerCase())) {
                return registeredCommand.execute(commandSender, s, strings);
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        for (CommandInterface registeredCommand : registeredCommands) {
            if (registeredCommand.getName().equals(command.getName().toLowerCase())) {
                return registeredCommand.tabComplete(commandSender, s, strings);
            }
        }
        return null;
    }
}
