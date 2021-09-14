package com.gmail.mariodeu2.ffa.commands;

import com.gmail.mariodeu2.ffa.PlayerDataStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.gmail.mariodeu2.ffa.Util.statsPrefix;

public class PointsCommand implements CommandInterface {
    @Override
    public @NotNull String getName() {
        return "points";
    }

    @Override
    public @NotNull String getUsage() {
        return "/points";
    }

    @Override
    public @NotNull String getDescription() {
        return "Shows the amount of points you have";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if(!(commandSender instanceof Player)) {
            return false;
        }

        commandSender.sendMessage(statsPrefix("points") + "   - " + PlayerDataStorage.getPlayerPoints((Player)commandSender));

        return true;
    }
}
