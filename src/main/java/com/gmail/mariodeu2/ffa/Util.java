package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.gmail.mariodeu2.ffa.Main.itemCreator;
import static com.gmail.mariodeu2.ffa.Settings.prefix;
import static com.gmail.mariodeu2.ffa.Settings.stat;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Util {

    public static void connectPlayer(Player player, String serverName) {
        var b = new ByteArrayOutputStream();
        var out = new DataOutputStream(b);

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

    public static ItemStack createItem(Material material, int amount, String displayName, String[] lore) {
        var item = new ItemStack(material, amount);

        var meta = item.getItemMeta();
        meta.setDisplayName(translateAlternateColorCodes('&', displayName));
        meta.setLore(Arrays.stream(lore).map(str -> translateAlternateColorCodes('&', str)).collect(Collectors.toList()));
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getItem(ModeManager.GameMode gameMode) {
        return itemCreator.gameModeItems[gameMode.ordinal()];
    }
}
