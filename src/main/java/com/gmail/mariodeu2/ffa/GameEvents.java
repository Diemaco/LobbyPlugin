package com.gmail.mariodeu2.ffa;

import com.destroystokyo.paper.ParticleBuilder;
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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static com.gmail.mariodeu2.ffa.Settings.*;
import static com.gmail.mariodeu2.ffa.main.*;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@SuppressWarnings("ConstantConditions")
public class GameEvents implements Listener {

    JavaPlugin plugin = main.getPlugin(main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(prefix.replace("FFA", "LOBBY") + ChatColor.GREEN + "" + ChatColor.BOLD + player.getName() + " joined the server!");

        player.getInventory().clear();

        switch (currentMode) {
            case STICK:
                player.getInventory().setItem(0, items.normalStick);
                break;
            case NOSTICKS:
                player.getInventory().setItem(0, new ItemStack(Material.AIR, 1));
                break;
            case SNOWBALL:
                player.getInventory().setItem(0, items.snowball);
                break;
            case SHOOTIE_SHOOT:
                player.getInventory().setItem(0, items.shootie_shoot);
                break;
            default:
        }

        player.getInventory().setItem(8, items.compassMenuItem);


        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).setBaseValue(500.0);
        player.teleport(lobbySpawnLocation, TeleportCause.PLUGIN);
        player.setGameMode(GameMode.ADVENTURE);

        ArrayList<Object> list = new ArrayList<>();

        list.add(0);                    // Kills
        list.add(0);                    // Deaths
        list.add(0);                    // Killstreak
        list.add(0D);                   // Points (Double)

        Database.stats.put(player, list);

        if (!Database.playerExists(player)) {
            Database.createPlayerProfile(player);
        }

        player.setLevel(Database.getPlayerKillstreak(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (playersAttacked.containsKey(event.getPlayer())) {

            for (Map.Entry<Player, attackedPlayer> set : playersAttacked.entrySet()) {
                if(set.getValue().getAttacker().equals(event.getPlayer())) {
                    ArrayList<Object> setPlayer = Database.stats.get(set.getKey());

                    setPlayer.set(3, (double) setPlayer.get(3) + 0.25);

                    Database.stats.replace(set.getKey(), setPlayer);
                    set.getKey().sendMessage(player_leave.replace("{player}", event.getPlayer().getName()).replace("{leave_bonus}", leave_bonus.toString()).replace("{prefix}", prefix));
                }
            }

            Player tagger = playersAttacked.get(event.getPlayer()).getAttacker();

            if (tagger == null) {
                playersAttacked.remove(event.getPlayer());
            }


            if (tagger.getWorld() == world) {
                ArrayList<Object> setTagger = Database.stats.get(tagger);

                setTagger.set(3, (double) setTagger.get(3) + 0.25);

                Database.stats.replace(tagger, setTagger);
                tagger.sendMessage(player_leave.replace("{player}", event.getPlayer().getName()).replace("{leave_bonus}", leave_bonus.toString()).replace("{prefix}", prefix));
            }

            playersAttacked.remove(event.getPlayer());
        }

        Database.updateAll(event.getPlayer());

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
        Database.updateAll(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (!(
                player instanceof Player &&
                attacker instanceof Player &&
                player.getWorld() == world) &&
                (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                        || event.getCause() == EntityDamageEvent.DamageCause.FIRE
                        || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)) {
            return;
        }


        event.setDamage(0);
        if (!playersAttacked.containsKey(player)) {
            attackedPlayer attackedPlayer = new attackedPlayer(player, attacker);

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

        if (!(
                player.getPlayer().getGameMode().equals(GameMode.ADVENTURE) &&
                player.getWorld() == world &&
                (!event.getTo().getBlock().getType().equals(Material.LAVA)) || player.getFireTicks() != 0)) {
            return;
        }

        killIfTagged(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void sniperUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Player playerHit;

        if (item == null || item.getType().equals(Material.DIAMOND_HOE)) {
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
    }

    public void killPlayer(Player player, Player tagger) {

        player.teleport(lobbySpawnLocation, TeleportCause.PLUGIN);
        player.setFireTicks(0);

        ArrayList<Object> set = Database.stats.get(player);
        set.set(1, (int) set.get(1) + 1);
        set.set(2, 0);
        // set.set(3, (double) set.get(3) + 1.0);

        Database.stats.replace(player, set);

        Objects.requireNonNull(player.getPlayer()).setLevel(0);

        try {
            player.sendActionBar(player_lose.replace("{player}", tagger.getName()));
            if (tagger.getWorld() == world) {
                ArrayList<Object> setTagger = Database.stats.get(tagger);
                setTagger.set(0, (int) setTagger.get(0) + 1);
                setTagger.set(2, (int) setTagger.get(2) + 1);
                setTagger.set(3, (double) setTagger.get(3) + 1.0);

                Database.stats.replace(tagger, setTagger);

                tagger.playSound(tagger.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100, 1);
                tagger.setLevel((int) setTagger.get(2));
                tagger.sendActionBar(player_win.replace("{player}", player.getName()));
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.toString());
        }
        playersAttacked.remove(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (currentMode.equals(gameMode.SNOWBALL) && event.getEntity() instanceof Snowball) {
            if (event.getEntity().getShooter() instanceof InventoryHolder) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> ((InventoryHolder) event.getEntity().getShooter()).getInventory().setItem(0, items.snowball), 1L);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }
        event.getPlayer().setSneaking(false);
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {

        if(
                event.getEntityType() != EntityType.PLAYER ||
                !(
                        event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) ||
                        event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))) {
            return;
        }

        Player player = (Player)event.getEntity();

        killIfTagged(player);
        event.setDamage(0);
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
                EntityDamageEvent newEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, 0);
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

        public Player getPlayer() {
            return player;
        }

        public Player getAttacker() {
            return attacker;
        }

        public Integer getSecondsLeft() {
            return Math.round(counter / 20f);
        }
    }
}