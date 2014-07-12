package com.bitbyterstudios.tenjava.devices;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class ChestDevice implements Device {
    private Zapped plugin;
    private Chest chest;
    private int power;

    public ChestDevice(Chest chest, int power, Zapped plugin) {
        this.plugin = plugin;
        this.chest = chest;
        this.power = power;
    }

    @Override
    public void power() {
        float fPower = power;
        for (int i = 0; i < chest.getInventory().getSize(); i++) {
            //Does the conversionConfig contain an entry for that item?
            ItemStack item = chest.getInventory().getItem(i);
            String itemName = item.getType().toString().toLowerCase();
            if (!plugin.getConversionConfig().contains(itemName)) {
                continue;
            }
            String[] split = plugin.getConversionConfig().getString(itemName).split(";");
            if (split.length != 2 || Utils.isFloat(split[1])) {
                plugin.getLogger().warning("Illegal entry for " + itemName + "!");
                continue;
            }
            ItemStack output;
            try {
                output = new ItemStack(Material.valueOf(split[0]));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Illegal entry for " + itemName + "(missing/wrong material)!");
                continue;
            }
            float cost = Float.valueOf(split[1]);
            while (fPower >= cost && item.getAmount() > 0) {
                fPower -= cost;
                item.setAmount(item.getAmount() -1);
                output.setAmount(output.getAmount() +1);
            }
            chest.getInventory().setItem(i, output);
            if (item.getAmount() > 0) {
                int slot = getFreeSlot();
                if (slot == -1) {
                    chest.getWorld().dropItem(chest.getLocation(), item);
                } else {
                    chest.getInventory().setItem(slot, item);
                }
            }
        }
    }

    private int getFreeSlot() {
        for (int i = 0; i < chest.getInventory().getSize(); i++) {
            if (chest.getInventory().getItem(i) == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Location getLocation() {
        return null;
    }
}
