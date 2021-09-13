package com.gmail.mariodeu2.ffa;

import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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

import static com.gmail.mariodeu2.ffa.Settings.prefix;
import static com.gmail.mariodeu2.ffa.main.items;

@SuppressWarnings("ConstantConditions")
public class InvEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = Bukkit.getPlayerExact(event.getWhoClicked().getName());

        if (!event.getWhoClicked().isOp()) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        if (!event.getInventory().contains(items.survivalTP)) return;


        if (main.playersAttacked.containsKey(player)) {
            player.sendMessage(prefix + ChatColor.BOLD + "" + ChatColor.GREEN + "You must wait " + main.playersAttacked.get(player).getSecondsLeft() + " seconds until you can use this command");
            return;
        }

        switch (event.getCurrentItem().getType()) {
            case GRASS_BLOCK:
                Util.connectPlayer(player, "survival");
                break;
            case DIAMOND_BLOCK:
                Util.connectPlayer(player, "creative");
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }

        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        if (event.getItem().getType().equals(Material.COMPASS)) {

            if(!AuthMeApi.getInstance().isAuthenticated(event.getPlayer())) {
                player.sendMessage("Please login to use this menu!");
                return;
            }

            items.updateMenu();
            player.openInventory(items.serverMenu);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getWhoClicked().isOp()) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        event.setCancelled(true);
    }
}