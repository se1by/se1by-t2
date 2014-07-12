package com.bitbyterstudios.tenjava.util;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.devices.ChestDevice;
import com.bitbyterstudios.tenjava.devices.Device;
import com.bitbyterstudios.tenjava.devices.FurnanceDevice;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class Utils {

    public static boolean isDevice(Block b) {
        switch (b.getType()) {
            case FURNACE:
            case CHEST:
                return true;
            default:
                return false;
        }
    }

    public static Device getDevice(Block b, int power, Zapped plugin) {
        if (!isDevice(b)) return null;
        if (b.getType().equals(Material.FURNACE)) {
            return new FurnanceDevice((org.bukkit.block.Furnace) b.getState(), power);
        } else if (b.getType().equals(Material.CHEST)) {
            return new ChestDevice((Chest) b.getState(), power, plugin);
        }
        return null;
    }

    public static BlockFace invert(BlockFace blockFace) {
        switch (blockFace) {
            case EAST: return BlockFace.WEST;
            case NORTH: return BlockFace.SOUTH;
            case SOUTH: return BlockFace.NORTH;
            case WEST: return BlockFace.WEST;
            default: return null;
        }
    }

    public static boolean isFloat(String s) {
        try {
            Float.valueOf(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
