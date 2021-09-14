package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.gmail.mariodeu2.ffa.Main.currentMode;
import static com.gmail.mariodeu2.ffa.Main.itemCreator;
import static com.gmail.mariodeu2.ffa.Settings.prefix;
import static com.gmail.mariodeu2.ffa.Settings.stat;

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

        player.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());

    }

    public static String statsPrefix(String text) {
        return stat.replace("{stat}", text).replace("{newline}", "\n");
    }

    public static void reloadItems() {
        switch (currentMode) {
            case SNOWBALL:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(itemCreator.normalStick);

                        player.getInventory().addItem(itemCreator.snowball);
                        continue;
                    }

                    player.getInventory().setItem(0, itemCreator.snowball);
                }
                break;
            case SHOOTIE_SHOOT:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(itemCreator.shootie_shoot);

                        player.getInventory().addItem(itemCreator.shootie_shoot);
                        continue;
                    }

                    player.getInventory().setItem(0, itemCreator.shootie_shoot);
                }
                break;
            case NOSTICKS:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(itemCreator.normalStick);
                        player.getInventory().remove(itemCreator.snowball);

                        continue;
                    }

                    player.getInventory().setItem(0, new ItemStack(Material.AIR, 1));
                }
                break;
            case STICK:
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE)) {
                        player.getInventory().remove(itemCreator.snowball);

                        player.getInventory().addItem(itemCreator.normalStick);
                        continue;
                    }

                    player.getInventory().setItem(0, itemCreator.normalStick);
                }
                break;
        }
    }

    public static ItemStack createItem(Material material, int amount, String displayName, String[] lore) {
        ItemStack itemStack = new ItemStack(material, amount);

        itemStack.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        itemStack.getItemMeta().setLore(Arrays.stream(lore).map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList()));

        return itemStack;
    }
}
