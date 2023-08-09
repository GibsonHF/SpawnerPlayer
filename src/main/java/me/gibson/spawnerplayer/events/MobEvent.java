package me.gibson.spawnerplayer.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.gibson.spawnerplayer.SpawnerPlayer;
import me.gibson.spawnerplayer.utils.Configuration;
import me.gibson.spawnerplayer.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobEvent
        implements Listener {
    private final List<String> worlds = Configuration.getConfig().getStringList("settings.allowed-worlds");
    private final List<UUID> spawnedPlayers = new ArrayList<UUID>();
    private final SpawnerPlayer plugin;

    public MobEvent(SpawnerPlayer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (this.worlds.contains(block.getWorld().getName()) || block.getType() != Material.WITHER_SKELETON_SKULL) {
            return;
        }
        this.spawnedPlayers.add(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.spawnedPlayers.remove(event.getPlayer().getUniqueId()), 20L);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType() != EntityType.WITHER || this.worlds.contains(entity.getLocation().getWorld().getName())) {
            return;
        }
        event.setCancelled(true);
        entity.getNearbyEntities(15.0, 15.0, 15.0).forEach(e -> {
            if (!this.spawnedPlayers.contains(e.getUniqueId())) {
                return;
            }
            this.spawnedPlayers.removeIf(x -> x.equals(e.getUniqueId()));
            Utils.sendMessage(e, Configuration.getString(Configuration.getConfig(), "lang.cannot-spawn-wither"));
        });
    }
}
