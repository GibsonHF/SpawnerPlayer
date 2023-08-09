package me.gibson.spawnerplayer.managers;


import me.gibson.spawnerplayer.SpawnerPlayer;

public abstract class Manager {
    public final SpawnerPlayer plugin;

    public Manager(SpawnerPlayer plugin) {
        this.plugin = plugin;
    }

    public abstract void load();

    public abstract void setup();

    public void close() {
    }
}
