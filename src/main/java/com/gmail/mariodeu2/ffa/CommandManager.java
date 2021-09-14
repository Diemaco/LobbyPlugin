package com.gmail.mariodeu2.ffa;

import com.gmail.mariodeu2.ffa.commands.CommandInterface;
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

    private final JavaPlugin plugin = Main.getPlugin(Main.class);
    private final List<CommandInterface> registeredCommands = new ArrayList<>();

    public void registerCommand(CommandInterface command) {
        if (!registeredCommands.contains(command)) {
            PluginCommand pluginCommand = plugin.getCommand(command.getName());

            if (pluginCommand != null && !pluginCommand.isRegistered()) {
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
        return registeredCommands.stream().filter(cmd -> cmd.getName() == command.getName()).findFirst().filter(cmd -> cmd.execute(commandSender, s, strings)).isPresent();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        return registeredCommands.stream().filter(cmd -> cmd.getName() == command.getName()).findFirst().map(cmd -> cmd.tabComplete(commandSender, s, strings)).orElse(null);
    }
}
