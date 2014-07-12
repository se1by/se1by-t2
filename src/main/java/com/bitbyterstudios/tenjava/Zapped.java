package com.bitbyterstudios.tenjava;

import com.bitbyterstudios.tenjava.listener.PlayerListener;
import com.bitbyterstudios.tenjava.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Zapped extends JavaPlugin {

    private HashMap<String, YamlConfiguration> worldConfigs;
    private HashMap<String, File> worldConfigFiles;

    private Messenger messenger;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        saveResource("messages.yml", false);
        messenger = new Messenger(this);
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public YamlConfiguration getWorldConfig(World world) {
        return getWorldConfig(world.getName());
    }

    public YamlConfiguration getWorldConfig(String world) {
        if (worldConfigs.get(world) == null) {
            loadWorldConfig(world);
        }
        return worldConfigs.get(world);
    }

    public void saveWorldConfig(World world) {
        saveWorldConfig(world.getName());
    }

    public void saveWorldConfig(String world) {
        if (worldConfigs.get(world) == null) {
            return;
        }
        try {
            worldConfigs.get(world).save(worldConfigFiles.get(world));
        } catch (IOException e) {
            getLogger().warning("Couldn't save worldConfig of " + world + "!");
            e.printStackTrace();
        }
        loadWorldConfig(world);
    }

    public void loadWorldConfig(String world) {
        if (Bukkit.getWorld(world) != null) {
            File file = new File(getDataFolder(), world + ".yml");
            worldConfigFiles.put(world, file);
            worldConfigs.put(world, YamlConfiguration.loadConfiguration(file));
        } else {
            throw new IllegalArgumentException("World " + world + " doesn't exists or isn't loaded!");
        }
    }

}
