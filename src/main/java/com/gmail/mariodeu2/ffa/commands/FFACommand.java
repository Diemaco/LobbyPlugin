package com.gmail.mariodeu2.ffa.commands;

import com.gmail.mariodeu2.ffa.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.mariodeu2.ffa.Main.currentMode;
import static com.gmail.mariodeu2.ffa.Main.itemCreator;
import static com.gmail.mariodeu2.ffa.Settings.command_wrong;
import static com.gmail.mariodeu2.ffa.Settings.prefix;
import static com.gmail.mariodeu2.ffa.Util.reloadItems;

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

        if(!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player)commandSender;

        if (args.length == 0) {
            player.sendMessage(command_wrong);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "snowball":
                currentMode = Main.gameMode.SNOWBALL;
                break;
            case "stick":
                currentMode = Main.gameMode.STICK;
                break;
            case "nostick":
                currentMode = Main.gameMode.NOSTICKS;
                break;
            case "shootie_shoot":
                currentMode = Main.gameMode.SHOOTIE_SHOOT;
                break;
            case "give":

                if (args.length < 2) {
                    player.sendMessage(command_wrong.replace("{prefix}", prefix));
                    return false;
                }

                switch (args[1].toLowerCase()) {
                    case "snowball":
                        player.getInventory().addItem(itemCreator.snowball);
                        break;
                    case "stick":
                        player.getInventory().addItem(itemCreator.normalStick);
                        break;
                    case "shootie_shoot":
                        player.getInventory().addItem(itemCreator.shootie_shoot);
                        break;
                    default:
                        player.sendMessage(command_wrong.replace("{prefix}", prefix));
                }
                break;
            default:
                player.sendMessage(command_wrong.replace("{prefix}", prefix));
        }

        reloadItems();

        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
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
}
