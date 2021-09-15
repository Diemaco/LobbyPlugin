package com.gmail.mariodeu2.ffa.commands;

import com.gmail.mariodeu2.ffa.ModeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.mariodeu2.ffa.Main.itemCreator;
import static com.gmail.mariodeu2.ffa.Settings.command_wrong;
import static com.gmail.mariodeu2.ffa.Settings.prefix;

public class FFACommand implements CommandInterface {
    @Override
    public @NotNull String getName() {
        return "ffa";
    }

    @Override
    public @NotNull String getUsage() {
        return "/ffa <argument> [argument]";
    }

    @Override
    public @NotNull String getDescription() {
        return "Admin commands for the FFA gamemode";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        if(!(commandSender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(command_wrong);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "snowball" ->
                    ModeManager.changeGameMode(ModeManager.GameMode.SNOWBALL);
            case "stick" ->
                    ModeManager.changeGameMode(ModeManager.GameMode.STICK);
            case "nostick" ->
                    ModeManager.changeGameMode(ModeManager.GameMode.NOSTICKS);
            case "shootie_shoot" ->
                    ModeManager.changeGameMode(ModeManager.GameMode.SHOOTIE_SHOOT);
            case "notouch" ->
                    ModeManager.changeGameMode(ModeManager.GameMode.CANT_TOUCH_THIS);
            case "give" -> {
                if (args.length < 2) {
                    player.sendMessage(command_wrong.replace("{prefix}", prefix));
                    return false;
                }
                switch (args[1].toLowerCase()) {
                    case "snowball" -> player.getInventory().addItem(itemCreator.gameModeItems[ModeManager.GameMode.SNOWBALL.ordinal()]);
                    case "stick" -> player.getInventory().addItem(itemCreator.gameModeItems[ModeManager.GameMode.STICK.ordinal()]);
                    case "shootie_shoot" -> player.getInventory().addItem(itemCreator.gameModeItems[ModeManager.GameMode.SHOOTIE_SHOOT.ordinal()]);
                    case "notouch" -> player.getInventory().addItem(itemCreator.hotSword);
                    default -> player.sendMessage(command_wrong.replace("{prefix}", prefix));
                }
            }
            default -> player.sendMessage(command_wrong.replace("{prefix}", prefix));
        }

        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
        List<String> l = new ArrayList<>();

        if (args.length == 1) {
            l.add("nostick");
            l.add("snowball");
            l.add("stick");
            l.add("notouch");
            l.add("give");
        }

        if (args.length == 2) {
            l.add("snowball");
            l.add("stick");
            l.add("shootie_shoot");
        }

        return l;
    }
}
