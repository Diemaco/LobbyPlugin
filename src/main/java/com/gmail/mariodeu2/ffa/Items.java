package com.gmail.mariodeu2.ffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Items {
    static Plugin plugin = Settings.plugin;
    public final ItemStack survivalTP = new ItemStack(Material.GRASS_BLOCK);
    public final ItemStack creativeTP = new ItemStack(Material.DIAMOND_BLOCK);

    public final ItemStack compassMenuItem = new ItemStack(Material.COMPASS);
    public final Inventory serverMenu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "" + ChatColor.BOLD + "Server menu");
    public final ItemStack shootie_shoot = new ItemStack(Material.DIAMOND_HOE);
    public ItemStack normalStick = new ItemStack(Material.STICK, 1);
    public ItemStack snowball = new ItemStack(Material.SNOWBALL, 16);

    public Items() {
        ItemMeta itemMetaSurvival = survivalTP.getItemMeta();
        List<String> lSurvival = new ArrayList<>();

        itemMetaSurvival.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lSurvival"));
        this.survivalTP.setItemMeta(itemMetaSurvival);

        lSurvival.add(ChatColor.translateAlternateColorCodes('&', "&aModified survival"));
        lSurvival.add(ChatColor.GREEN + "Version: 1.17.1");
        this.survivalTP.setLore(lSurvival);


        ItemMeta itemMetaCreative = this.creativeTP.getItemMeta();
        List<String> lCreative = new ArrayList<>();

        itemMetaCreative.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lCreative"));
        this.creativeTP.setItemMeta(itemMetaCreative);

        lCreative.add(ChatColor.translateAlternateColorCodes('&', "&aBuild in creative on plots"));
        lCreative.add(ChatColor.GREEN + "Version: 1.17.1");
        this.creativeTP.setLore(lCreative);


        // ItemMeta itemMetaPixelmon = this.pixelmonTP.getItemMeta();
        // List<String> lPixelmon = new ArrayList<>();

        // itemMetaPixelmon.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lPixelmon"));
        // this.pixelmonTP.setItemMeta(itemMetaPixelmon);

        // lPixelmon.add(ChatColor.translateAlternateColorCodes('&', "&cEnjoy playing the pixelmon mod"));
        // lPixelmon.add(ChatColor.RED + "Version: 1.12.2");
        // this.pixelmonTP.setLore(lPixelmon);


        ItemMeta itemMetaKompas = this.compassMenuItem.getItemMeta();
        List<String> lKompas = new ArrayList<>();

        itemMetaKompas.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Menu");
        this.compassMenuItem.setItemMeta(itemMetaKompas);

        lKompas.add(ChatColor.GOLD + "Opens the server menu");
        this.compassMenuItem.setLore(lKompas);


        // this.serverMenu.setItem(11, this.pixelmonTP);
        this.serverMenu.setItem(13, this.survivalTP);
        this.serverMenu.setItem(15, this.creativeTP);


        ItemMeta itemMetaStick = this.normalStick.getItemMeta();
        List<String> lStick = new ArrayList<>();

        // this.normalStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);

        itemMetaStick.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Knockback Stick");
        lStick.add(ChatColor.GOLD + "Shove it up everyone's ...");

        itemMetaStick.setLore(lStick);
        this.normalStick.setItemMeta(itemMetaStick);


        ItemMeta itemMetaSnowball = this.snowball.getItemMeta();
        List<String> lSnowball = new ArrayList<>();

        // this.snowball.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        itemMetaSnowball.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Knockback Snowball");

        lSnowball.add(ChatColor.GOLD + "1. Hit someone");
        lSnowball.add(ChatColor.GOLD + "2. Don't get hit");
        lSnowball.add(ChatColor.GOLD + "3. Player gets close? Throw a snowball at him!");
        lSnowball.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Repeat");
        itemMetaSnowball.setLore(lSnowball);
        this.snowball.setItemMeta(itemMetaSnowball);


        ItemMeta itemMetaTest = this.shootie_shoot.getItemMeta();
        List<String> lTest = new ArrayList<>();

        itemMetaTest.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Shootie shoot");


        lTest.add(ChatColor.GOLD + "It's not overpowered. You're just good");
        itemMetaTest.setLore(lTest);
        this.shootie_shoot.setItemMeta(itemMetaTest);
    }

    public void updateMenu() {

        // this.serverMenu.setItem(11, this.pixelmonTP);
        this.serverMenu.setItem(13, this.survivalTP);
        this.serverMenu.setItem(15, this.creativeTP);
        /*
        ItemStack pixelmon = serverMenu.getItem(11);

        List<String> pixelmonLore = pixelmon.getLore();
        pixelmonLore.set(2, ChatColor.translateAlternateColorCodes('&', String.format("&4&lPlayers online: ", new BungeeChannelApi(plugin).getPlayerCount("survival").join())));

        pixelmon.setLore(pixelmonLore);
        serverMenu.setItem(11, pixelmon);



        ItemStack survival = serverMenu.getItem(13);
        List<String> survivalLore = survival.getLore();
        survivalLore.set(2, ChatColor.translateAlternateColorCodes('&', String.format("&2&lPlayers online: ", new BungeeChannelApi(plugin).getPlayerCount("survival").join())));
        survival.setLore(survivalLore);
        serverMenu.setItem(13, survival);



        ItemStack creative = serverMenu.getItem(15);

        List<String> creativeLore = creative.getLore();
        survivalLore.set(2, ChatColor.translateAlternateColorCodes('&', String.format("&2&lPlayers online: ", new BungeeChannelApi(plugin).getPlayerCount("survival").join())));

        creative.setLore(creativeLore);
        serverMenu.setItem(15, creative);
        */
    }
}
