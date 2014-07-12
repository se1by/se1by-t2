package com.bitbyterstudios.tenjava;

import com.bitbyterstudios.tenjava.listener.PlayerListener;
import com.bitbyterstudios.tenjava.listener.WeatherListener;
import com.bitbyterstudios.tenjava.util.Messenger;
import com.bitbyterstudios.tenjava.util.WeatherRunnable;
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
    private YamlConfiguration conversionConfig;
    private File conversionConfigFile;

    private HashMap<String, WeatherRunnable> weatherRunnables;

    private Messenger messenger;

    @Override
    public void onEnable() {
        saveResource("messages.yml", false);
        saveResource("conversion.yml", false);
        messenger = new Messenger(this);
        worldConfigs = new HashMap<>();
        worldConfigFiles = new HashMap<>();
        weatherRunnables = new HashMap<>();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
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
            boolean empty = !file.exists();
            worldConfigFiles.put(world, file);
            worldConfigs.put(world, YamlConfiguration.loadConfiguration(file));
            if (empty) {
                worldConfigs.get(world).set("chance", 0.2);
                worldConfigs.get(world).set("interval", 10);
                saveWorldConfig(world);
            }
        } else {
            throw new IllegalArgumentException("World " + world + " doesn't exists or isn't loaded!");
        }
    }

    public YamlConfiguration getConversionConfig() {
        if (conversionConfig == null) {
            conversionConfigFile = new File(getDataFolder(), "conversion.yml");
            conversionConfig = YamlConfiguration.loadConfiguration(conversionConfigFile);
        }
        return conversionConfig;
    }

    public void saveConversionConfig() {
        try {
            conversionConfig.save(conversionConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, WeatherRunnable> getWeatherRunnables() {
        return weatherRunnables;
    }
}
