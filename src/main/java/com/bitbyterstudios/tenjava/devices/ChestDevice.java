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
            ItemStack item = chest.getInventory().getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) continue;
            String itemName = item.getType().toString().toLowerCase();
            //Does the conversionConfig contain an entry for that item?
            if (!plugin.getConversionConfig().contains(itemName)) {
                continue;
            }
            String[] split = plugin.getConversionConfig().getString(itemName).split(";");
            if (split.length != 2 || !Utils.isFloat(split[1])) {
                plugin.getLogger().warning("Illegal entry for " + itemName + "!");
                continue;
            }
            ItemStack output;
            try {
                output = new ItemStack(Material.valueOf(split[0].toUpperCase()), 0);
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
            if (item.getAmount() <= 0) {
                chest.getInventory().setItem(i, output);
            } else {
                chest.getInventory().setItem(i, item);
                for (int x = 0; x < chest.getInventory().getSize(); x++) {
                    //empty slot
                    if (chest.getInventory().getItem(x) == null || chest.getInventory().getItem(x).getType().equals(Material.AIR)) {
                        chest.getInventory().setItem(x, output);
                        return;
                    }
                    //Same type
                    if (chest.getInventory().getItem(x).getType().equals(output.getType())) {
                        if (output.getMaxStackSize() - chest.getInventory().getItem(x).getAmount() >= output.getAmount()) {
                            output.setAmount(output.getAmount() + chest.getInventory().getItem(x).getAmount());
                            chest.getInventory().setItem(x, output);
                            return;
                        } else {
                            output.setAmount(output.getAmount() + chest.getInventory().getItem(x).getAmount() - output.getMaxStackSize());
                            chest.getInventory().setItem(x, new ItemStack(output.getType(), output.getMaxStackSize()));
                        }
                    }
                }
                if (output.getAmount() > 0) {
                    chest.getWorld().dropItem(chest.getLocation(), output);
                }
            }
        }
    }


    @Override
    public Location getLocation() {
        return null;
    }
}
