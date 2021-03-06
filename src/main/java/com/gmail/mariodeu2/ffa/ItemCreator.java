package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

import static com.gmail.mariodeu2.ffa.Util.createItem;

public class ItemCreator {

    public final ItemStack[] gameModeItems = {
            createItem(Material.STICK, 1, "&6&lKnockback Stick", new String[]{
                    "&eShove it up everyone's #@!"
            }),
            new ItemStack(Material.AIR, 1),
            createItem(Material.SNOWBALL, 16, "&6&lKnockback Snowball", new String[]{
                    "&e1. Hit someone", "&e2. Don't get hit", "&e3. Someone gets too close? Throw a snowball!", "&e&lRepeat"
            }),
            createItem(Material.DIAMOND_HOE, 1, "&6&lShootie Shoot", new String[]{
                    "&e&lIt's not overpowered. You're just good"
            }),
            new ItemStack(Material.AIR, 1)
    };

    public final ItemStack survivalMenuItem =
            createItem(Material.GRASS_BLOCK, 1, "&2&lSurvival", new String[]{
                "&aModified Survival", "&aVersion: N/A"
            });

    public final ItemStack creativeMenuItem =
            createItem(Material.DIAMOND_BLOCK, 1, "&1&lCreative", new String[]{
                "&9Creative Building", "&9Version: N/A"
            });

    public final ItemStack compassMenuOpenItem =
            createItem(Material.COMPASS, 1, "&6&lMenu", new String[]{
                "&eOpens the server menu", "&eJust right click while holding it"
            });

    public final Inventory serverMenu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "Server menu");


    public final ItemStack hotSword =
            createItem(Material.GOLDEN_SWORD, 1, "&4&lHot Fucking Sword", new String[]{
                    "&c&lUsed to set things or people on fire", "&c&lKeep away from explosives"
            });

    public final ItemStack[] redArmorSet = {
            new ItemStack(Material.LEATHER_HELMET),
            new ItemStack(Material.LEATHER_CHESTPLATE),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.LEATHER_BOOTS)
    };

    public ItemCreator() {

        var meta = (LeatherArmorMeta) redArmorSet[0].getItemMeta();
        meta.setColor(Color.RED);
        meta.setUnbreakable(true);

        Arrays.stream(redArmorSet).forEach(itemStack -> itemStack.setItemMeta(meta));

        this.gameModeItems[ModeManager.GameMode.STICK.ordinal()].addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        this.gameModeItems[ModeManager.GameMode.SNOWBALL.ordinal()].addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
        this.hotSword.addEnchantment(Enchantment.FIRE_ASPECT, 2);

        this.serverMenu.setItem(12, this.survivalMenuItem);
        this.serverMenu.setItem(14, this.creativeMenuItem);
    }

    public void updateMenu() {
        this.serverMenu.setItem(12, this.survivalMenuItem);
        this.serverMenu.setItem(14, this.creativeMenuItem);
    }
}
