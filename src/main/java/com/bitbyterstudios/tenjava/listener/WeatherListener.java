package com.bitbyterstudios.tenjava.listener;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.util.WeatherRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class WeatherListener implements Listener {

    private Zapped plugin;

    public WeatherListener(Zapped plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            WeatherRunnable weatherRunnable = new WeatherRunnable(plugin, event.getWorld());
            plugin.getWeatherRunnables().put(event.getWorld().getName(), weatherRunnable);
            long interval = plugin.getWorldConfig(event.getWorld()).getLong("interval");
            plugin.getWeatherRunnables().get(event.getWorld().getName()).runTaskTimer(plugin, interval*20, interval*20);
        } else {
            if (plugin.getWeatherRunnables().get(event.getWorld().getName()) != null) {
                plugin.getWeatherRunnables().get(event.getWorld().getName()).cancel();

            }
        }
    }
}
