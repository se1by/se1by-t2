package com.bitbyterstudios.tenjava.listener;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.util.MessagingKeys;
import com.bitbyterstudios.tenjava.util.SafeLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

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

        plugin.getMessenger().send(event.getPlayer(), MessagingKeys.LIGHTNINGROD_CREATED);
        saveLightningRod(event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.BREWING_STAND)) {
            return;
        }
        if (isLightningRod(event.getBlock())) {
            deleteLightningRod(event.getBlock().getLocation());
            plugin.getMessenger().send(event.getPlayer(), MessagingKeys.LIGHTNINGROD_DESTROYED);
        }
    }

    /**
     * Checks if the block is a lightning rod.
     * @param block The block to check. Has to be the top block (brewing stand)
     * @return True if it is a lightning rod, false if not
     */
    private boolean isLightningRod(Block block) {
        return (block.getType().equals(Material.BREWING_STAND) &&
                block.getRelative(0, -1, 0).getType().equals(Material.COBBLE_WALL) &&
                block.getRelative(0, -2, 0).getType().equals(Material.COBBLE_WALL)) ||
                plugin.getWorldConfig(block.getWorld()).getStringList("rods").contains(new SafeLocation(block.getLocation()).toString());
    }

    /**
     * Saves the lightning rod in the world's config
     * @param loc The location of the lightning rod
     */
    private void saveLightningRod(Location loc) {
        List<String> list = plugin.getWorldConfig(loc.getWorld()).getStringList("rods");
        list.add(new SafeLocation(loc).toString());
        plugin.getWorldConfig(loc.getWorld()).set("rods", list);
        plugin.saveWorldConfig(loc.getWorld());
    }

    /**
     * Deletes a lightning rod from the world's config
     * @param loc The location of the lightning rod to be deleted
     */
    private void deleteLightningRod(Location loc) {
        List<String> list = plugin.getWorldConfig(loc.getWorld()).getStringList("rods");
        list.remove(new SafeLocation(loc).toString());
        plugin.getWorldConfig(loc.getWorld()).set("rods", list);
        plugin.saveWorldConfig(loc.getWorld());
    }
}
