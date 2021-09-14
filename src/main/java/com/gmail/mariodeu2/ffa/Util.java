package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static com.gmail.mariodeu2.ffa.Settings.prefix;
import static com.gmail.mariodeu2.ffa.Settings.stat;
import static com.gmail.mariodeu2.ffa.main.currentMode;
import static com.gmail.mariodeu2.ffa.main.items;

public class Util {

    public static void connectPlayer(Player player, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            Bukkit.getServer().broadcastMessage(prefix.replace("FFA", "LOBBY") + ChatColor.GREEN + "" + ChatColor.BOLD + player.getName() + " connected to " + serverName);
        } catch (Exception e) {
            assert player != null;
            player.sendMessage(prefix.replace("FFA", "LOBBY") + ChatColor.RED + "There was an problem connecting to the " + serverName + " server!");
            return;
        }

        player.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());

    }

    public static String statsPrefix(String text) {
        return stat.replace("{stat}", text).replace("{newline}", "\n");
    }

    public static void reloadItems() {
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
}
