package com.bitbyterstudios.tenjava.util;

import com.bitbyterstudios.tenjava.Zapped;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class WeatherRunnable extends BukkitRunnable {
    private Zapped plugin;
    private World world;

    private static final Random random = new Random();

    public WeatherRunnable(Zapped plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    @Override
    public void run() {
        if (random.nextDouble() <= plugin.getWorldConfig(world).getDouble("chance")) {
            List<String> list = plugin.getWorldConfig(world).getStringList("rods");
            SafeLocation safeLocation = SafeLocation.fromString(list.get(random.nextInt(list.size())));
            world.strikeLightningEffect(safeLocation.toLocation());
        }
    }
}
