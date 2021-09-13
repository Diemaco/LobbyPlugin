package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static com.gmail.mariodeu2.ffa.Settings.prefix;

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
}
