package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemCreator {
    public final ItemStack survivalMenuItem;
    public final ItemStack creativeMenuItem;

    public final ItemStack compassMenuOpenItem;
    public final Inventory serverMenu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "Server menu");


    public final ItemStack shootie_shoot;
    public final ItemStack normalStick;
    public final ItemStack snowball;


    public ItemCreator() {
        survivalMenuItem = Util.createItem(Material.GRASS_BLOCK, 1, "&2&lSurvival",new String[]{
                "&aModified Survival", "&aVersion: 1.17.1"
        });

        creativeMenuItem = Util.createItem(Material.DIAMOND_BLOCK, 1, "&1&lCreative",new String[]{
                "&9Creative Building", "&9Version: 1.17.1"
        });

        compassMenuOpenItem = Util.createItem(Material.COMPASS, 1, "&6&lMenu",new String[]{
                "&eOpens the server menu", "&eJust right click while holding it"
        });

        this.normalStick = Util.createItem(Material.STICK, 1, "&6&lKnockback Stick",new String[]{
                "&eShove it up everyone's ..."
        });

        this.snowball = Util.createItem(Material.SNOWBALL, 16, "&6&lKnockback Snowball",new String[]{
                "&e1. Hit someone", "&e2. Don't get hit", "&e3. Someone gets too close? Throw a snowball!", "&e&lRepeat"
        });

        this.shootie_shoot = Util.createItem(Material.DIAMOND_HOE, 1, "&6&lShootie Shoot",new String[]{
                "&e&lIt's not overpowered. You're just good"
        });

        this.serverMenu.setItem(12, this.survivalMenuItem);
        this.serverMenu.setItem(14, this.creativeMenuItem);
    }

    public void updateMenu() {
        this.serverMenu.setItem(13, this.survivalMenuItem);
        this.serverMenu.setItem(15, this.creativeMenuItem);
    }
}
