package com.bitbyterstudios.tenjava.util;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.devices.BatteryDevice;
import com.bitbyterstudios.tenjava.devices.ChestDevice;
import com.bitbyterstudios.tenjava.devices.Device;
import com.bitbyterstudios.tenjava.devices.FurnaceDevice;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class Utils {

    /**
     * Checks if a block is a device. Ignores location, so if only says if it COULD be a device.
     * @param b The block to be checked.
     * @return True if the block is a device, false if not
     */
    public static boolean isDevice(Block b) {
        switch (b.getType()) {
            case FURNACE:
            case CHEST:
            case LAPIS_BLOCK:
                return true;
            default:
                return false;
        }
    }

    /**
     * Gets a device instance for the supplied block.
     * @param b The block to convert.
     * @param power How much power got this device?
     * @param plugin A plugin reference to give it to the device
     * @return The device if the block is a device, else null
     */
    public static Device getDevice(Block b, int power, Zapped plugin) {
        if (!isDevice(b)) return null;
        if (b.getType().equals(Material.FURNACE)) {
            return new FurnaceDevice((org.bukkit.block.Furnace) b.getState(), power);
        } else if (b.getType().equals(Material.CHEST)) {
            return new ChestDevice((Chest) b.getState(), power, plugin);
        } else if (b.getType().equals(Material.LAPIS_BLOCK)) {
            return new BatteryDevice(plugin, b, power);
        }
        return null;
    }

    /**
     * Inverts the blockface
     * @param blockFace The blockface to get inverted
     * @return The inverted blockface
     */
    public static BlockFace invert(BlockFace blockFace) {
        switch (blockFace) {
            case EAST: return BlockFace.WEST;
            case NORTH: return BlockFace.SOUTH;
            case SOUTH: return BlockFace.NORTH;
            case WEST: return BlockFace.WEST;
            default: return null;
        }
    }

    /**
     * Checks if the supplied string is a float
     * @param s The string to be checked
     * @return True if it is a float, false if not
     */
    public static boolean isFloat(String s) {
        try {
            Float.valueOf(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
