package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemCreator {
    public final ItemStack survivalMenuItem =
            Util.createItem(Material.GRASS_BLOCK, 1, "&2&lSurvival", new String[]{
                "&aModified Survival", "&aVersion: 1.17.1"
    });

    public final ItemStack creativeMenuItem =
            Util.createItem(Material.DIAMOND_BLOCK, 1, "&1&lCreative", new String[]{
                "&9Creative Building", "&9Version: 1.17.1"
    });

    public final ItemStack compassMenuOpenItem =
            Util.createItem(Material.COMPASS, 1, "&6&lMenu", new String[]{
                "&eOpens the server menu", "&eJust right click while holding it"
    });

    public final Inventory serverMenu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "Server menu");


    public final ItemStack shootie_shoot =
            Util.createItem(Material.DIAMOND_HOE, 1, "&6&lShootie Shoot", new String[]{
                "&e&lIt's not overpowered. You're just good"
    });

    public final ItemStack stick =
            Util.createItem(Material.STICK, 1, "&6&lKnockback Stick", new String[]{
                "&eShove it up everyone's ..."
    });

    public final ItemStack snowball =
            Util.createItem(Material.SNOWBALL, 16, "&6&lKnockback Snowball", new String[]{
                "&e1. Hit someone", "&e2. Don't get hit", "&e3. Someone gets too close? Throw a snowball!", "&e&lRepeat"
    });


    public ItemCreator() {
        this.stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        this.snowball.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        this.serverMenu.setItem(12, this.survivalMenuItem);
        this.serverMenu.setItem(14, this.creativeMenuItem);
    }

    public void updateMenu() {
        this.serverMenu.setItem(12, this.survivalMenuItem);
        this.serverMenu.setItem(14, this.creativeMenuItem);
    }
}
