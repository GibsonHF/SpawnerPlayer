package me.gibson.spawnerplayer.managers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import me.gibson.spawnerplayer.SpawnerPlayer;
import me.gibson.spawnerplayer.utils.Utils;
import org.bukkit.Location;

public class DatabaseManager
        extends Manager {
    private final List<Location> spawners = new ArrayList<Location>();
    private Connection connection;

    public DatabaseManager(SpawnerPlayer plugin) {
        super(plugin);
        this.setup();
        this.load();
    }

    @Override
    public void setup() {
        try {
            File file = new File(this.plugin.getDataFolder(), "database.db");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Connection con = this.connect();
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS spawners(  id INTEGER PRIMARY KEY AUTOINCREMENT,  location LONGTEXT)").execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM spawners");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Location location = Utils.toLocation(rs.getString("location"));
                this.spawners.add(location);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        File file = new File(this.plugin.getDataFolder(), "database.db");
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        return this.connection;
    }

    public Connection getConnection() throws Exception {
        boolean closed;
        try {
            closed = !this.connection.isValid(5);
        }
        catch (Throwable ex) {
            closed = this.connection.isClosed();
        }
        return closed ? this.connect() : this.connection;
    }

    public List<Location> getSpawners() {
        return this.spawners;
    }

    public void addSpawner(Location location) {
        this.spawners.add(location);
        try {
            Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO spawners(location) VALUES(?)");
            ps.setString(1, Utils.toString(location));
            ps.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeSpawner(Location location) {
        this.spawners.remove(location);
        try {
            Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM spawners WHERE location = ?");
            ps.setString(1, Utils.toString(location));
            ps.executeUpdate();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
