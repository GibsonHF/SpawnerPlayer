package me.gibson.spawnerplayer;

import me.gibson.spawnerplayer.events.AnvilEvent;
import me.gibson.spawnerplayer.events.BlockEvent;
import me.gibson.spawnerplayer.events.MobEvent;
import me.gibson.spawnerplayer.managers.DatabaseManager;
import me.gibson.spawnerplayer.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerPlayer
        extends JavaPlugin {
    private DatabaseManager databaseManager;

    public void onEnable() {
        new Configuration(this).load();
        this.databaseManager = new DatabaseManager(this);
        Bukkit.getPluginManager().registerEvents(new BlockEvent(this.databaseManager), this);
        Bukkit.getPluginManager().registerEvents(new MobEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new AnvilEvent(this), this);
    }

    public void onDisable() {
        this.databaseManager = null;
    }
}
