package com.bitbyterstudios.tenjava.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Jonas Seibert on 12.07.2014.
 * All rights reserved.
 */
public class SafeLocation {
    private int x, y , z;
    private String world;

    private static final JSONParser parser = new JSONParser();

    public SafeLocation(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
    }

    public SafeLocation(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Location toLocation() {
        if (Bukkit.getWorld(world) == null) {
            throw new IllegalStateException("World " + world + " is not loaded or doesn't exist!");
        }
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("z", z);
        jsonObject.put("world", world);
        return jsonObject.toJSONString();
    }

    public static SafeLocation fromString(String s) {
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(s);
            if (!jsonObject.containsKey("x")|| !jsonObject.containsKey("y") || !jsonObject.containsKey("z")
                    || !jsonObject.containsKey("world")) {
                throw new IllegalArgumentException("Provided string is missing some keys!");
            }
            return new SafeLocation(((Long) jsonObject.get("x")).intValue(), ((Long) jsonObject.get("y")).intValue(),
                    ((Long) jsonObject.get("z")).intValue(), (String) jsonObject.get("world"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Provided string " + s + " is not a jsonString!");
    }
}
