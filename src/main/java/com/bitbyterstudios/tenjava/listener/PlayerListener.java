package com.bitbyterstudios.tenjava.listener;

import com.bitbyterstudios.tenjava.Zapped;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class PlayerListener implements Listener {

    private final Zapped plugin;

    public PlayerListener(Zapped plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getBlockPlaced().getType().equals(Material.BREWING_STAND) || !isLightningRod(event.getBlock())) {
            return;
        }

        //Save it somewhere (mysqlDB, mongoDB or yaml)
    }

    private boolean isLightningRod(Block block) {
        return block.getType().equals(Material.BREWING_STAND) &&
                block.getRelative(0, -1, 0).getType().equals(Material.COBBLE_WALL) &&
                block.getRelative(0, -2, 0).getType().equals(Material.COBBLE_WALL);
    }
}
