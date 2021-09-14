package com.gmail.mariodeu2.ffa.commands;

import com.gmail.mariodeu2.ffa.PlayerDataStorage;
import com.gmail.mariodeu2.ffa.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatsCommand implements CommandInterface {
    @Override
    public @NotNull String getName() {
        return "stats";
    }

    @Override
    public @NotNull String getUsage() {
        return "/stats";
    }

    @Override
    public @NotNull String getDescription() {
        return "View your FFA stats";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        if(!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player)commandSender;

        int kills = PlayerDataStorage.getPlayerKills(player);
        int deaths = PlayerDataStorage.getPlayerDeaths(player);

        player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "---------|" + ChatColor.DARK_RED + " FFA Stats " + ChatColor.RED + "|---------");
        player.sendMessage(Util.statsPrefix("kills") + "   - " + kills);
        player.sendMessage(Util.statsPrefix("deaths") + "   - " + deaths);
        player.sendMessage(Util.statsPrefix("killstreak") + "   - " + PlayerDataStorage.getPlayerKills(player));
        player.sendMessage(Util.statsPrefix("K/D Ratio") + "   - " + (double) Math.round((double) kills / (double) deaths * 100) / 100);
        player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "---------|" + ChatColor.DARK_RED + " FFA Stats " + ChatColor.RED + "|---------");

        return true;
    }
}
