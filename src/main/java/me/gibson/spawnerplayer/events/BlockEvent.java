package me.gibson.spawnerplayer.events;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.gibson.spawnerplayer.managers.DatabaseManager;
import me.gibson.spawnerplayer.utils.Configuration;
import me.gibson.spawnerplayer.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockEvent
        implements Listener {
    private final DatabaseManager manager;
    private final Map<Player, Location> locations;

    public BlockEvent(DatabaseManager manager) {
        this.manager = manager;
        this.locations = new ConcurrentHashMap<Player, Location>();
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.SPAWNER) {
            // Check if the world is not in the enabled worlds list
            if (!Configuration.getEnabledWorlds().contains(event.getBlock().getWorld().getName())) {
                // If the player doesn't have a silk touch pickaxe, prevent the spawner from dropping
                    event.setDropItems(false);
                    player.sendMessage(Utils.color("&cSpawners won't drop in this world!"));
                    return;
            }
        }
        Location lastLocation;
        Block block = event.getBlock();
        if (block == null || !block.getType().name().contains("SPAWNER")) {
            return;
        }
        Location location = block.getLocation();
        if (location == null || this.manager.getSpawners().contains(location)) {
            return;
        }
        if (this.locations.containsKey(event.getPlayer()) && (lastLocation = this.locations.remove(event.getPlayer())) != null && lastLocation.equals(location)) {
            block.setType(Material.AIR);
            return;
        }
        event.setCancelled(true);
        Utils.sendMessage(event.getPlayer(), Configuration.getString(Configuration.getConfig(), "lang.cannot-break"));
        this.locations.put(event.getPlayer(), location);
        Bukkit.getScheduler().runTaskLater(this.manager.plugin, () -> this.locations.remove(event.getPlayer()), 30L);

    }

    @EventHandler
    public void blockBreak(BlockExplodeEvent event) {
        event.blockList().removeIf(b -> !this.manager.getSpawners().contains(b.getLocation()) && b.getType().name().contains("SPAWNER"));
    }

    @EventHandler
    public void blockBreak(EntityExplodeEvent event) {
        event.blockList().removeIf(b -> !this.manager.getSpawners().contains(b.getLocation()) && b.getType().name().contains("SPAWNER"));
    }

    @EventHandler
    public void blockBreak(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block == null || !block.getType().name().contains("SPAWNER")) {
            return;
        }
        this.manager.addSpawner(block.getLocation());
    }
}