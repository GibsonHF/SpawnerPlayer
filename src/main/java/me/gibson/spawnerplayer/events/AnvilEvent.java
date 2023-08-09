package me.gibson.spawnerplayer.events;

import java.util.List;
import java.util.stream.Collectors;

import me.gibson.spawnerplayer.SpawnerPlayer;
import me.gibson.spawnerplayer.utils.Configuration;
import me.gibson.spawnerplayer.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class AnvilEvent
        implements Listener {
    private final List<Material> materials = Configuration.getConfig().getStringList("settings.blocked-items").stream().map(x -> Material.getMaterial(x.toUpperCase())).collect(Collectors.toList());
    private final SpawnerPlayer plugin;

    public AnvilEvent(SpawnerPlayer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerRenameItem(InventoryClickEvent event) {
        if (event.getView().getType() != InventoryType.ANVIL) {
            return;
        }
        ItemStack itemStack = event.getView().getItem(0);
        if (itemStack == null || !this.materials.contains(itemStack.getType())) {
            return;
        }
        if (event.getRawSlot() != 2) {
            return;
        }
        event.setCancelled(true);
        Utils.sendMessage(event.getWhoClicked(), Configuration.getString(Configuration.getConfig(), "lang.cannot-rename-item"));
    }
}
