package com.bitbyterstudios.tenjava.util;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.devices.Device;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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

            //Just a little optical gimmick, not actually needed
            world.strikeLightningEffect(safeLocation.toLocation());
            for (Block b : getCables(getGroundBlock(safeLocation.toLocation()))) {
                power(b);
                Collection<Device> devices = getDevices(b, new HashMap<String, Device>(), 10, BlockFace.SELF).values();
                for (Device device : devices) {
                    device.power();
                }
            }
        }
    }

    private Block getGroundBlock(Location loc) {
        while (loc.getBlock().getType().equals(Material.COBBLE_WALL) || loc.getBlock().getType().equals(Material.BREWING_STAND)) {
            loc.setY(loc.getY() -1);
        }
        loc.setY(loc.getY() +1);
        return loc.getBlock();
    }

    private List<Block> getCables(Block b) {
        List<Block> cables = new ArrayList<>();
        if (b.getRelative(BlockFace.EAST).getType().equals(Material.REDSTONE_WIRE)) {
            cables.add(b.getRelative(BlockFace.EAST));
        }
        if (b.getRelative(BlockFace.NORTH).getType().equals(Material.REDSTONE_WIRE)) {
            cables.add(b.getRelative(BlockFace.NORTH));
        }
        if (b.getRelative(BlockFace.SOUTH).getType().equals(Material.REDSTONE_WIRE)) {
            cables.add(b.getRelative(BlockFace.SOUTH));
        }
        if (b.getRelative(BlockFace.WEST).getType().equals(Material.REDSTONE_WIRE)) {
            cables.add(b.getRelative(BlockFace.WEST));
        }
        return cables;
    }

    private HashMap<String, Device> getDevices(Block b, HashMap<String, Device> list, int power, BlockFace from) {
        if (power == 0) return list;
        if (Utils.isDevice(b) && !list.containsKey(b.getLocation().toString())) { //Don't kill me for that quickfix :o
            list.put(b.getLocation().toString(), Utils.getDevice(b, power));
        } else {
            if (b.getType().equals(Material.REDSTONE_WIRE)) {
                if (!from.equals(BlockFace.EAST)) {
                    list.putAll(getDevices(b.getRelative(BlockFace.EAST), list, power-1, Utils.invert(BlockFace.EAST)));
                }
                if (!from.equals(BlockFace.NORTH)) {
                    list.putAll(getDevices(b.getRelative(BlockFace.NORTH), list, power-1, Utils.invert(BlockFace.NORTH)));
                }
                if (!from.equals(BlockFace.SOUTH)) {
                    list.putAll(getDevices(b.getRelative(BlockFace.SOUTH), list, power-1, Utils.invert(BlockFace.SOUTH)));
                }
                if (!from.equals(BlockFace.WEST)) {
                    list.putAll(getDevices(b.getRelative(BlockFace.WEST), list, power-1, Utils.invert(BlockFace.WEST)));
                }
            }
        }
        return list;
    }

    private void power(final Block b) {
        final Material mat = b.getRelative(BlockFace.DOWN, 2).getType();
        b.getRelative(BlockFace.DOWN, 2).setType(Material.REDSTONE_TORCH_ON);
        new BukkitRunnable() {

            @Override
            public void run() {
                b.getRelative(BlockFace.DOWN, 2).setType(mat);
            }
        }.runTaskLater(plugin, 2);
    }
}
