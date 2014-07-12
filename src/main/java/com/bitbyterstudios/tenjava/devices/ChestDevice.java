package com.bitbyterstudios.tenjava.devices;

import com.bitbyterstudios.tenjava.Zapped;
import com.bitbyterstudios.tenjava.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 *
 * A chest device is a chest next to a power line. If it contains items mentioned in conversion.yml,
 * it will convert them at the cost mentioned in conversion.yml. If it contains spawn eggs mentioned in
 * conversion.yml, it will spawn them at that cost.
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
            if (itemName.equals("monster_egg")) {
                fPower = spawn(item, itemName, fPower, i);
            } else if (itemName.equals("dragon_egg")) {
                fPower = spawnDragon(item, itemName, fPower, i);
            } else {
                fPower = convert(item, itemName, fPower, i);
            }
        }
    }

    /**
     * Converts the item to the type as in conversion.yml
     * @param item The original item
     * @param itemName The name of the item
     * @param fPower current power that can be used
     * @param index The inventory index
     * @return power left after converting
     */
    private float convert(ItemStack item, String itemName, float fPower, int index) {
        String[] split = plugin.getConversionConfig().getString(itemName).split(";");
        if (split.length != 2 || !Utils.isFloat(split[1])) {
            plugin.getLogger().warning("Illegal entry for " + itemName + "!");
            return fPower;
        }
        ItemStack output;
        try {
            output = new ItemStack(Material.valueOf(split[0].toUpperCase()), 0);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Illegal entry for " + itemName + "(missing/wrong material)!");
            return fPower;
        }
        float cost = Float.valueOf(split[1]);
        while (fPower >= cost && item.getAmount() > 0) {
            fPower -= cost;
            item.setAmount(item.getAmount() - 1);
            output.setAmount(output.getAmount() + 1);
        }

        //This monster puts the items back to the chest^^
        chest.getInventory().setItem(index, item);
        int slot = -1;
        for (int x = 0; x < chest.getInventory().getSize(); x++) {
            //empty slot
            if (chest.getInventory().getItem(x) == null || chest.getInventory().getItem(x).getType().equals(Material.AIR)) {
                //We want the first free slot
                if (slot == -1) {
                    slot = x;
                }
                continue;
            }
            //Same type
            if (chest.getInventory().getItem(x).getType().equals(output.getType())) {
                if (output.getMaxStackSize() - chest.getInventory().getItem(x).getAmount() >= output.getAmount()) {
                    output.setAmount(output.getAmount() + chest.getInventory().getItem(x).getAmount());
                    chest.getInventory().setItem(x, output);
                    return fPower;
                } else {
                    output.setAmount(output.getAmount() + chest.getInventory().getItem(x).getAmount() - output.getMaxStackSize());
                    chest.getInventory().setItem(x, new ItemStack(output.getType(), output.getMaxStackSize()));
                }
            }
        }
        if (output.getAmount() > 0) {
            if (slot != -1) {
                chest.getInventory().setItem(slot, output);
            } else {
                chest.getWorld().dropItem(chest.getLocation(), output);
            }
        }
        return fPower;
    }

    /**
     * Spawns a monster. Cost defined in conversion.yml
     * @param item The original item (monster egg)
     * @param itemName The name of the item
     * @param fPower current power that can be used
     * @param index The inventory index
     * @return power left after spawning
     */
    private float spawn(ItemStack item, String itemName, float fPower, int index) {
        SpawnEgg se = (SpawnEgg) item.getData();
        String s = plugin.getConversionConfig().getString(itemName + "." + se.getSpawnedType().toString().toLowerCase());
        float cost = Float.valueOf(s);
        while (fPower >= cost && item.getAmount() > 0) {
            fPower -= cost;
            chest.getWorld().spawn(chest.getLocation().add(0, 2, 0), se.getSpawnedType().getEntityClass());
            item.setAmount(item.getAmount() -1);
        }
        if (item.getAmount() > 0) {
            chest.getInventory().setItem(index, item);
        } else {
            chest.getInventory().setItem(index, null);
        }
        return fPower;
    }

    /**
     * We'll do that later...
     * @param item
     * @param itemName
     * @param fPower
     * @param index
     * @return
     */
    private float spawnDragon(ItemStack item, String itemName, float fPower, int index) {
        //TODO: add dragon spawning!!!!111!!oneeleven!
        return fPower;
    }


    @Override
    public Location getLocation() {
        return null;
    }
}
