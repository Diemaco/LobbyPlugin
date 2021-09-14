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

    public static ItemStack createItem(Material material, int amount, String displayName, String[] lore) {
        ItemStack itemStack = new ItemStack(material, amount);

        itemStack.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        itemStack.getItemMeta().setLore(Arrays.stream(lore).map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList()));

        return itemStack;
    }
}
