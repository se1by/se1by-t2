package com.bitbyterstudios.tenjava.devices;

import org.bukkit.Location;
import org.bukkit.block.Furnace;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 *
 * A furnace device receives cooking power from electricity!
 * Up to the equivalent of a lava bucket
 */
public class FurnaceDevice implements Device {
    private Furnace furnace;
    private int power;

    public FurnaceDevice(Furnace furnace, int power) {
        this.furnace = furnace;
        this.power = power;
    }

    @Override
    public void power() {
        furnace.setBurnTime((short) (power * 2000));
        furnace.update();
    }

    @Override
    public Location getLocation() {
        return furnace.getLocation();
    }
}
