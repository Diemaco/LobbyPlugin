package com.gmail.mariodeu2.ffa;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.concurrent.ThreadLocalRandom;

import static com.gmail.mariodeu2.ffa.Main.itemCreator;

public class GameModes {

    public static gameMode currentGameMode = gameMode.STICK;
    public static Player selectedPlayer = null;

    public static void changeGameMode(gameMode newGameMode) {
        ItemStack newItem = switch (newGameMode) {
            case SNOWBALL -> itemCreator.snowball;
            case SHOOTIE_SHOOT -> itemCreator.shootie_shoot;
            case CANT_TOUCH_THIS, NOSTICKS -> new ItemStack(Material.AIR, 1);
            case STICK -> itemCreator.stick;
        };

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().setItem(0, newItem);

            player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
            player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
            player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
            player.getInventory().setBoots(new ItemStack(Material.AIR, 1));
        }

        if (newGameMode == gameMode.CANT_TOUCH_THIS) {
            selectedPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[ThreadLocalRandom.current().nextInt(Bukkit.getOnlinePlayers().size())];

            Bukkit.broadcastMessage(
                    ChatColor.translateAlternateColorCodes('&', "&4&lCan't touch this mode! Everyone but &4'" + selectedPlayer.getName() + "'&4&l has a weapon! Just don't get hit and survive."));
            selectedPlayer.sendTitle(new Title(ChatColor.translateAlternateColorCodes('&', "&4&lYou're it"), ChatColor.translateAlternateColorCodes('&', "&c&lYou can't be hit, everyone else can.")));

            ItemStack LeatherHelmet = new ItemStack(Material.LEATHER_HELMET);
            ItemStack LeatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            ItemStack LeatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
            ItemStack LeatherBoots = new ItemStack(Material.LEATHER_BOOTS);

            LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet.getItemMeta();
            meta.setColor(Color.RED);
            LeatherHelmet.setItemMeta(meta);
            LeatherChestplate.setItemMeta(meta);
            LeatherLeggings.setItemMeta(meta);
            LeatherBoots.setItemMeta(meta);

            selectedPlayer.getInventory().setHelmet(LeatherHelmet);
            selectedPlayer.getInventory().setChestplate(LeatherChestplate);
            selectedPlayer.getInventory().setLeggings(LeatherLeggings);
            selectedPlayer.getInventory().setBoots(LeatherBoots);

            ItemStack sword = Util.createItem(Material.GOLDEN_SWORD, 1, "&4&lHot Fucking Sword", new String[]{
                    "&a&lUsed to set things or people on fire", "&a&lKeep away from explosives"
            });

            sword.addEnchantment(Enchantment.FIRE_ASPECT, 1);

            selectedPlayer.getInventory().setItem(0, sword);
        }
    }

    public enum gameMode {
        STICK,
        NOSTICKS,
        SNOWBALL,
        SHOOTIE_SHOOT,
        // HOT_POTATO,
        CANT_TOUCH_THIS
    }
}
