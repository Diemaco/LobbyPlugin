package com.gmail.mariodeu2.ffa;

import com.destroystokyo.paper.ParticleBuilder;
import com.destroystokyo.paper.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.mariodeu2.ffa.Main.itemCreator;
import static com.gmail.mariodeu2.ffa.Main.lock;
import static com.gmail.mariodeu2.ffa.ModeManager.*;
import static com.gmail.mariodeu2.ffa.PlayerDataStorage.*;
import static com.gmail.mariodeu2.ffa.Settings.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@SuppressWarnings("ConstantConditions")
public class GameEvents implements Listener {

    public static final HashMap<Player, attackedPlayer> playersAttacked = new HashMap<>();
    private final JavaPlugin plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Create player settings if player settings haven't been created before
        if (!playerExists(player)) {
            createPlayerProfile(player);
        }

        // Clear inventory
        player.getInventory().clear();

        // Set slot 8 to the compass menu item
        player.getInventory().setItem(8, itemCreator.compassMenuOpenItem);

        // Enable 1.8 combat for player
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(500.0);

        // Set gamemode to the adventure mode
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);

        // Teleport to world spawn
        player.teleport(lobbySpawnLocation, TeleportCause.PLUGIN);

        // Set join message
        event.setJoinMessage(prefix.replace("FFA", "LOBBY") + GREEN + "" + BOLD + player.getName() + " joined the server!");

        // Give item depending on current game mode
        player.getInventory().setItem(0, itemCreator.gameModeItems[currentGameMode.ordinal()]);

        // Notify player if the 'Can't touch this' game mode is active
        if (ModeManager.currentGameMode == ModeManager.GameMode.CANT_TOUCH_THIS) {
            player.sendTitle(new Title(
                    translateAlternateColorCodes('&', "&4&lCan't touch this!"),
                    translateAlternateColorCodes('&', "&4&l" + selectedPlayer.getName() + "&4 has a weapon and can't be hit. The game is over when he dies"), 40, 80, 40));
        }

        // Deserialize the player's data and cache it
        cachedPlayerData.put(player, PlayerData.deserialize(player));

        // Set the player's xp level to their killstreak
        player.setLevel(getPlayerKillstreak(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (playersAttacked.containsKey(event.getPlayer())) {

            for (Map.Entry<Player, attackedPlayer> set : playersAttacked.entrySet()) {
                if(set.getValue().getAttacker().equals(event.getPlayer())) {
                    var setPlayer = cachedPlayerData.get(set.getKey());

                    setPlayer.points += 0.25;

                    cachedPlayerData.replace(set.getKey(), setPlayer);
                    set.getKey().sendMessage(player_leave.replace("{player}", event.getPlayer().getName()).replace("{leave_bonus}", leave_bonus.toString()).replace("{prefix}", prefix));
                }
            }

            Player tagger = playersAttacked.get(event.getPlayer()).getAttacker();

            if (tagger.getWorld() == world) {
                PlayerData setTagger = cachedPlayerData.get(tagger);

                setTagger.points += 0.25;

                cachedPlayerData.replace(tagger, setTagger);
                tagger.sendMessage(player_leave.replace("{player}", event.getPlayer().getName()).replace("{leave_bonus}", leave_bonus.toString()).replace("{prefix}", prefix));
            }

            playersAttacked.remove(event.getPlayer());
        }

        saveAndClear(event.getPlayer());

        Bukkit.getScheduler().runTaskLater(plugin, () -> changeGameMode(ModeManager.GameMode.CANT_TOUCH_THIS), 0L);

        event.setQuitMessage("");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        if (playersAttacked.containsKey(event.getPlayer())) {

            Player tagger = playersAttacked.get(event.getPlayer()).getAttacker();

            if (tagger == null) {
                playersAttacked.remove(event.getPlayer());
            }

            playersAttacked.remove(event.getPlayer());
        }
        saveAndClear(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (!(
                player instanceof Player &&
                        attacker instanceof Player &&
                        event.getCause() == DamageCause.ENTITY_ATTACK)) {
            return;
        }

        if(selectedPlayer == player) {
            event.setCancelled(true);
        }

        event.setDamage(0);
        if (!playersAttacked.containsKey(player)) {
            var attackedPlayer = new attackedPlayer(player, attacker);

            try {
                lock.lock();

                playersAttacked.put(player, attackedPlayer);
            } finally {
                lock.unlock();
            }

            Integer id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, attackedPlayer::run, 0, 0);
            attackedPlayer.setId(id);
        } else {
            try {
                lock.lock();

                if (playersAttacked.get(player).getAttacker() == attacker) {
                    playersAttacked.get(player).reset();
                } else {
                    playersAttacked.get(player).stop();

                    attackedPlayer attackedPlayer = new attackedPlayer(player, attacker);
                    playersAttacked.remove(player);
                    playersAttacked.put(player, attackedPlayer);

                    Integer id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, attackedPlayer::run, 0, 0);
                    attackedPlayer.setId(id);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @EventHandler()
    public void onProjectileHit(ProjectileHitEvent event) {

        Entity snowball = event.getEntity();
        ProjectileSource shooter = event.getEntity().getShooter();
        Entity hitEntity = event.getHitEntity();

        if (snowball instanceof Snowball && hitEntity instanceof Player && shooter instanceof Player) {
            if (!shooter.equals(hitEntity)) {
                ((Player) event.getHitEntity()).damage(1, (Player) event.getEntity().getShooter());
                ((Player) event.getHitEntity()).setHealth(20);
            }
        }
    }

    @EventHandler
    public void onPlayerLose(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if ((
                !player.getPlayer().getGameMode().equals(org.bukkit.GameMode.ADVENTURE) ||
                !event.getTo().getBlock().getType().equals(Material.LAVA))) {
            return;
        }

        if(player.getFireTicks() == 0) {
            return;
        }

        killIfTagged(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void sniperUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Player playerHit;

        if (item == null || !item.getType().equals(Material.DIAMOND_HOE)) {
            return;
        }

        player.setCooldown(Material.DIAMOND_HOE, 15);
        Location eyeLocation = player.getEyeLocation();
        RayTraceResult result = player.getWorld().rayTraceEntities(eyeLocation, eyeLocation.getDirection(), 1000, 0.0, entity -> entity instanceof Player && !entity.getName().equals(player.getName()));

        double x1 = eyeLocation.getX();
        double y1 = eyeLocation.getY();
        double z1 = eyeLocation.getZ();
        double x2;
        double y2;
        double z2;

        if (result == null) {
            result = player.getWorld().rayTraceBlocks(eyeLocation, eyeLocation.getDirection(), 1000, FluidCollisionMode.NEVER, true);
            if (result == null) {
                return;
            }
            Location HitPosition = result.getHitPosition().toLocation(world);
            x2 = HitPosition.getX();
            y2 = HitPosition.getY();
            z2 = HitPosition.getZ();
        } else {
            playerHit = (Player) result.getHitEntity();
            assert playerHit != null;
            eyeLocation = playerHit.getEyeLocation();
            x2 = eyeLocation.getX();
            y2 = eyeLocation.getY();
            z2 = eyeLocation.getZ();

            killPlayer(playerHit, player);
            playerHit.teleport(lobbySpawnLocation, TeleportCause.PLUGIN);
        }

        double distance = Math.pow(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2), 0.5);
        Location location = player.getEyeLocation();
        Vector dir = location.getDirection();

        for (int i = 0; i < distance * 10; i++) {
            dir.normalize();
            dir.multiply(0.1);
            location.add(dir);

            ParticleBuilder particle = new ParticleBuilder(Particle.FIREWORKS_SPARK);
            particle.allPlayers();
            particle.count(0);
            particle.force(true);
            particle.location(location.set(location.getX(), 60D, location.getZ()));
            particle.offset(0.0, 0.0, 0.0);
            particle.spawn();
        }
    }

    public void killIfTagged(Player player) {
        if (playersAttacked.containsKey(player)) {
            killPlayer(player, playersAttacked.get(player).getAttacker());
        } else {
            player.teleport(lobbySpawnLocation, TeleportCause.PLUGIN);
            player.sendActionBar(player_fall);
        }

        if(player == selectedPlayer) {
            selectedPlayer = null;
            changeGameMode(ModeManager.GameMode.STICK);

            Bukkit.broadcastMessage(
                    translateAlternateColorCodes('&', "&4&l'" + player.getName() + "'&4&l died and your sticks have been given back"));
        }
    }

    public void killPlayer(Player player, Player tagger) {

        //  Teleport to lobby spawn
        player.teleport(lobbySpawnLocation, TeleportCause.PLUGIN);

        // Extinguish player if needed
        player.setFireTicks(0);

        // Get the cached playerdata
        var cachedData = cachedPlayerData.get(player);

        // Increase the player's deaths
        cachedData.deaths += 1;

        // Set the player's killstreak to 0
        cachedData.killstreak = 0;

        // Set the player's xp bar to the cached killstreak (0 because he died)
        player.getPlayer().setLevel(cachedData.killstreak);

        // Send the player lose message to the player's actionbar
        player.sendActionBar(player_lose.replace("{player}", tagger.getName()));

        // If the player is online change his stats
        if(tagger.isOnline() && cachedPlayerData.containsKey(tagger)) {

            // Get the cached player data
            var setTagger = cachedPlayerData.get(tagger);

            // Increase the tagger's kills by 1
            setTagger.kills += 1;

            // Increase the tagger's killstreak by 1
            setTagger.killstreak += 1;

            // Increase the tagger's points by 1
            setTagger.points += 1.0D;

            // Play the sound
            tagger.playSound(tagger.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100, 1);

            // Set the taggers xp bar level to the taggers killstreak
            tagger.setLevel(setTagger.killstreak);

            // Send the player win message to the
            tagger.sendActionBar(player_win.replace("{player}", player.getName()));
        }

        // Remove the player from the attacked players list
        playersAttacked.remove(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (
                currentGameMode == ModeManager.GameMode.SNOWBALL
                && event.getEntity() instanceof Snowball
                && event.getEntity().getShooter() instanceof InventoryHolder)
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                    ((InventoryHolder) event.getEntity().getShooter()).getInventory().setItem(0, Util.getItem(currentGameMode)), 1L
            );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }
        event.setCancelled(true);
        event.getPlayer().setSneaking(false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (
                event.getEntityType() != EntityType.PLAYER
                || !(event.getCause() == DamageCause.FIRE
                || event.getCause() == DamageCause.FIRE_TICK
                || event.getCause() == DamageCause.LAVA)) {
            return;
        }

        event.setDamage(0);

        killIfTagged((Player)event.getEntity());
    }

    public static class attackedPlayer {

        private final Player player;
        private final Player attacker;
        private int counter = damageTimeout;
        private Integer id;

        public attackedPlayer(Player player, Player attacker) {
            this.player = player;
            this.attacker = attacker;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void run() {

            if (counter > 0) {
                counter--;
            } else {
                var newEvent = new EntityDamageEvent(player, DamageCause.VOID, 0);
                player.setLastDamageCause(newEvent);

                this.stop();
                playersAttacked.remove(player);
            }
        }

        public void reset() {
            counter = damageTimeout;
        }

        public void stop() {
            Bukkit.getScheduler().cancelTask(id);
        }

        public Player getAttacker() {
            return attacker;
        }

        public Integer getSecondsLeft() {
            return Math.round(counter / 20f);
        }
    }
}