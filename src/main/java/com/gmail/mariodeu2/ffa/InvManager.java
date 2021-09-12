package com.gmail.mariodeu2.ffa;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Objects;

import static com.gmail.mariodeu2.ffa.config.prefix;
import static com.gmail.mariodeu2.ffa.main.items;

public class InvManager implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = Bukkit.getPlayerExact(event.getWhoClicked().getName());


        if (!Objects.requireNonNull(Bukkit.getPlayerExact(event.getWhoClicked().getName())).getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        if (!event.getInventory().contains(items.survivalTP)) return;


        if (main.playersAttacked.containsKey(player)) {
            assert player != null;
            player.sendMessage(prefix + ChatColor.BOLD + "" + ChatColor.GREEN + "You must wait " + main.playersAttacked.get(player).getSecondsLeft() + " seconds until you can use this command");
            return;
        }
        // https://github.com/filoghost/HolographicDisplays
        // https://github.com/filoghost/HolographicDisplays/wiki/Basic-tutorial
        // https://filoghost.me/
        // https://ci.codemc.io/job/filoghost/job/HolographicDisplays/javadoc/index.html?com/gmail/filoghost/holographicdisplays/api/HologramsAPI.html

        if (Objects.equals(event.getCurrentItem(), items.survivalTP)) {
            connectPlayer(player, "survival");
        }

        if (Objects.equals(event.getCurrentItem(), items.creativeTP)) {
            connectPlayer(player, "creative");
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Objects.requireNonNull(event.getItem()).getType();
        } catch (Exception e) {
            return;
        }

        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        if (Objects.requireNonNull(event.getItem()).getType().equals(Material.COMPASS)) {
            if (playerLocation.equals(new Location(Bukkit.getWorld("world"), -5, 51, 6, playerLocation.getYaw(), playerLocation.getPitch()))) {
                player.sendMessage("You must move first to open this menu!");
                return;
            }

            items.updateMenu();
            player.openInventory(items.serverMenu);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!Objects.requireNonNull(Bukkit.getPlayerExact(event.getWhoClicked().getName())).getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        if (event.getInventory().equals(items.serverMenu)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        event.setCancelled(true);
    }


    public void connectPlayer(Player player, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            // Bukkit.getLogger().info(prefix.replace("FFA", "LOBBY") + ChatColor.GREEN + "" + ChatColor.BOLD + player.getName() + " connected to " + serverName);

        } catch (Exception e) {
            assert player != null;
            player.sendMessage(prefix.replace("FFA", "LOBBY") + ChatColor.RED + "There was an problem connecting to the " + serverName + " server!");
            return;
        }

        assert player != null;
        player.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());

        // Bukkit.getServer().broadcastMessage(prefix.replace("FFA", "LOBBY") + ChatColor.GREEN + "" + ChatColor.BOLD + player.getName() + " connected to " + serverName);
    }
}
