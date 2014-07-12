package com.bitbyterstudios.tenjava.devices;

import com.bitbyterstudios.tenjava.Zapped;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class BatteryDevice implements Device {
    private static final String BATTERY_KEY = "Zapped-Battery_Key";

    private Zapped plugin;
    private Block battery;
    private int power;

    public BatteryDevice(Zapped plugin, Block battery, int power) {
        this.plugin = plugin;
        this.battery = battery;
        this.power = power;
    }

    @Override
    public void power() {
        int prevPower = 0;
        if (battery.hasMetadata(BATTERY_KEY)) {
            prevPower = battery.getMetadata(BATTERY_KEY).get(0).asInt();
        }
        battery.setMetadata(BATTERY_KEY, new FixedMetadataValue(plugin, prevPower + power));
        if (getSign() != null) {
            getSign().setLine(2, (prevPower + power) + "");
            getSign().update(true);
            System.out.println("set sign");
        } else {
            System.out.println("no sign :(");
        }
    }

    private Sign getSign() {
        if (battery.getRelative(BlockFace.EAST).getState() instanceof Sign) {
            return (Sign) battery.getRelative(BlockFace.EAST).getState();
        }
        if (battery.getRelative(BlockFace.NORTH).getState() instanceof Sign) {
            return (Sign) battery.getRelative(BlockFace.NORTH).getState();
        }
        if (battery.getRelative(BlockFace.SOUTH).getState() instanceof Sign) {
            return (Sign) battery.getRelative(BlockFace.SOUTH).getState();
        }
        if (battery.getRelative(BlockFace.WEST).getState() instanceof Sign) {
            return (Sign) battery.getRelative(BlockFace.WEST).getState();
        }
        return null;
    }

    @Override
    public Location getLocation() {
        return battery.getLocation();
    }
}
