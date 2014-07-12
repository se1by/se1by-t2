package com.bitbyterstudios.tenjava.devices;

import org.bukkit.Location;
import org.bukkit.block.Furnace;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class FurnanceDevice implements Device {
    private Furnace furnace;
    private int power;

    public FurnanceDevice(Furnace furnace, int power) {
        this.furnace = furnace;
        this.power = power;
    }

    @Override
    public void power() {
        System.out.println("powering with " + (power * 2000) + "fuel");
        furnace.setBurnTime((short) (power * 2000));
        furnace.update();
    }

    @Override
    public Location getLocation() {
        return furnace.getLocation();
    }
}
