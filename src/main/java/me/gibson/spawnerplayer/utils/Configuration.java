package me.gibson.spawnerplayer.utils;

import java.io.File;
import java.lang.reflect.Field;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {
    private final JavaPlugin plugin;
    private static FileConfiguration config;

    public Configuration(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        try {
            File dataFolder = this.plugin.getDataFolder();
            this.load(new File(dataFolder, "config.yml"), this.getClass().getDeclaredField("config"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void load(File file, Field field) throws Exception {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            this.plugin.saveResource(file.getName(), false);
        }
        field.set(null, YamlConfiguration.loadConfiguration(file));
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static String getString(ConfigurationSection section, String s) {
        if (section == null) {
            return "";
        }
        return section.getString(s, "");
    }

    public static String getString(YamlConfiguration config, String s) {
        return config.getString(s, "");
    }
}
