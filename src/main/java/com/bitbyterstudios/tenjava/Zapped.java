package com.bitbyterstudios.tenjava;

import com.bitbyterstudios.tenjava.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Zapped extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

}
