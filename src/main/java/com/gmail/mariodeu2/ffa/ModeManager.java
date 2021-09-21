package com.gmail.mariodeu2.ffa;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

import static com.gmail.mariodeu2.ffa.Main.itemCreator;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class ModeManager {

    public static GameMode currentGameMode = GameMode.STICK;
    public static Player selectedPlayer = null;

    public static void changeGameMode(GameMode newGameMode) {

        currentGameMode = newGameMode;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();

            player.getInventory().setItem(0, Util.getItem(currentGameMode));
            player.getInventory().setItem(8, itemCreator.compassMenuOpenItem);
        }

        if (newGameMode == GameMode.CANT_TOUCH_THIS) {
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                changeGameMode(GameMode.STICK);
                return;
            }

            selectedPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[ThreadLocalRandom.current().nextInt(Bukkit.getOnlinePlayers().size())];

            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player == selectedPlayer) {
                    selectedPlayer.sendTitle(new Title(translateAlternateColorCodes('&', "&4&lYou're it"), translateAlternateColorCodes('&', "&c&lYou can't be hit, everyone else can.")));

                } else {
                    player.sendTitle(new Title(
                            translateAlternateColorCodes('&', "&4&lCan't touch this!"),
                            translateAlternateColorCodes('&', "&4&l" + selectedPlayer.getName() + "&4 has a weapon and can't be hit. The game is over when he dies"), 40, 80, 40));
                }
            }

            selectedPlayer.getInventory().setHelmet(itemCreator.redArmorSet[0]);
            selectedPlayer.getInventory().setChestplate(itemCreator.redArmorSet[1]);
            selectedPlayer.getInventory().setLeggings(itemCreator.redArmorSet[2]);
            selectedPlayer.getInventory().setBoots(itemCreator.redArmorSet[3]);

            selectedPlayer.getInventory().setItem(0, itemCreator.hotSword);
        }
    }

    public enum GameMode {
        STICK,              // Everyone has a stick
        NOSTICKS,           // No one has a stick
        SNOWBALL,           // Everyone has snowballs
        SHOOTIE_SHOOT,      // Everyone has shootie shoots
        CANT_TOUCH_THIS,    // One player has red armor and can't be hit. They also have a sword with fire aspect
        // HOT_POTATO,      // Tnt run but with potatoes and lava
    }
}
