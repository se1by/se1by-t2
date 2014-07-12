package com.bitbyterstudios.tenjava.util;

import com.bitbyterstudios.tenjava.Zapped;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class Messenger {

    private static final String PREFIX = "§1[§6Zapped§1]§r ";

    private YamlConfiguration config;

    public Messenger(Zapped plugin) {
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "messages.yml"));
    }

    public void send(CommandSender sender, String key) {
        sender.sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', config.getString(key)));
    }
}
