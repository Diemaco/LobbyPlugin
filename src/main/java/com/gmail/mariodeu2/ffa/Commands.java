package com.gmail.mariodeu2.ffa;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.mariodeu2.ffa.Settings.*;
import static com.gmail.mariodeu2.ffa.main.*;
import static org.bukkit.Bukkit.getPlayerExact;

public class Commands implements @Nullable CommandExecutor, @NotNull Listener, @Nullable TabCompleter {

    Plugin plugin = getPlugin(main.class);

    String statsPrefix(String text) {
        return stat.replace("{stat}", text).replace("{newline}", "\n");
    }

    void reloadItems() {

        switch (currentMode) {
            case SNOWBALL:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(items.normalStick);

                        player.getInventory().addItem(items.snowball);
                        continue;
                    }

                    player.getInventory().setItem(0, items.snowball);
                }
                break;
            case SHOOTIE_SHOOT:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(items.shootie_shoot);

                        player.getInventory().addItem(items.shootie_shoot);
                        continue;
                    }

                    player.getInventory().setItem(0, items.shootie_shoot);
                }
                break;
            case NOSTICKS:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(items.normalStick);
                        player.getInventory().remove(items.snowball);

                        continue;
                    }

                    player.getInventory().setItem(0, new ItemStack(Material.AIR, 1));
                }
                break;
            case STICK:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(items.snowball);

                        player.getInventory().addItem(items.normalStick);
                        continue;
                    }

                    player.getInventory().setItem(0, items.normalStick);
                }
                break;
        }
    }

    // Points command
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = getPlayerExact(commandSender.getName());
        assert player != null;

        if (command.getName().equalsIgnoreCase("ffa")) {

            try {
                if (strings[0].equals("test")) {
                }
            } catch (Exception e) {
                player.sendMessage(command_wrong);
                return false;
            }

            switch (strings[0].toLowerCase()) {
                case "snowball":
                    currentMode = gameMode.SNOWBALL;
                    reloadItems();
                    break;
                case "stick":
                    currentMode = gameMode.STICK;
                    reloadItems();
                    break;
                case "nostick":
                    currentMode = gameMode.NOSTICKS;
                    reloadItems();
                    break;
                case "shootie_shoot":
                    currentMode = gameMode.SHOOTIE_SHOOT;
                    reloadItems();
                    break;
                case "give":

                    try {
                        if (strings[0].equals("shootie_shoot")) {
                        }
                    } catch (Exception e) {
                        player.sendMessage(command_wrong.replace("{prefix}", prefix));
                        return false;
                    }

                    switch (strings[1].toLowerCase()) {
                        case "snowball":
                            player.getInventory().addItem(items.snowball);
                            break;
                        case "stick":
                            player.getInventory().addItem(items.normalStick);
                            break;
                        case "shootie_shoot":
                            player.getInventory().addItem(items.shootie_shoot);
                            break;
                        default:
                            player.sendMessage(command_wrong.replace("{prefix}", prefix));
                    }
                    break;
                default:
                    player.sendMessage(command_wrong.replace("{prefix}", prefix));
            }

            return true;
        }

        /*
        if (command.getName().equalsIgnoreCase("stats")) {
            PersistentDataContainer data = player.getPersistentDataContainer();

            Integer killstreak = data.get(NSKKillstreak, PersistentDataType.INTEGER);
            Integer kills = data.get(NSKKills, PersistentDataType.INTEGER);
            Integer deaths = data.get(NSKDeaths, PersistentDataType.INTEGER);

            player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "---------|" + ChatColor.DARK_RED + " FFA Stats " + ChatColor.RED + "|---------");
            player.sendMessage(statsPrefix("kills") + "   - " + kills);
            player.sendMessage(statsPrefix("deaths") + "   - " + deaths);
            player.sendMessage(statsPrefix("killstreak") + "   - " + killstreak);
            player.sendMessage(statsPrefix("K/D Ratio") + "   - " + (double) Math.round(kills.floatValue() / deaths.floatValue() * 100) / 100);
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "---------|" + ChatColor.DARK_RED + " FFA Stats " + ChatColor.RED + "|---------");

        } else if (command.getName().equalsIgnoreCase("points")) {
            PersistentDataContainer data = player.getPersistentDataContainer();
            Double points = data.get(NSKPoints, PersistentDataType.DOUBLE);
            player.sendMessage(statsPrefix("points") + "   - " + points);
            return true;
        } else {
            commandSender.sendMessage(command_wrong.replace("{prefix}", prefix));
        }
        */

        commandSender.sendMessage("This feature has been disabled :(");
        return true;

    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("ffa")) {
            List<String> l = new ArrayList<>();

            if (args.length == 1) {
                l.add("nostick");
                l.add("snowball");
                l.add("stick");
                l.add("give");
            }

            if (args.length == 2) {
                l.add("snowball");
                l.add("stick");
                l.add("shootie_shoot");
            }

            return l;
        }

        return null;
    }
}