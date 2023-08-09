package me.gibson.spawnerplayer.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(CommandSender sender, String s) {
        if (s == null || s.trim().isEmpty() || sender == null) {
            return;
        }
        s = Utils.color(s);
        if (!(sender instanceof Player)) {
            Bukkit.getServer().getConsoleSender().sendMessage(s);
            return;
        }
        sender.sendMessage(s);
    }

    public static Location toLocation(String s) {
        String[] split = s.split(",");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public static String toString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }
}
