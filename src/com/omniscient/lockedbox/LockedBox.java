package com.omniscient.lockedbox;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Skin;
import com.omniscient.lockedbox.Commands.LBAdmin;
import com.omniscient.lockedbox.Commands.LBGiveKey;
import com.omniscient.lockedbox.Listeners.BoxListener;
import com.omniscient.lockedbox.Listeners.InventoryListener;
import com.omniscient.lockedbox.Listeners.ChatListener;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.UUID;

public class LockedBox extends JavaPlugin {
    public static LockedBox plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getDataFolder().mkdir();

        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BoxListener(), this);

        Bukkit.getServer().getPluginCommand("lbadmin").setExecutor(new LBAdmin());
        Bukkit.getServer().getPluginCommand("lbgivekey").setExecutor(new LBGiveKey());

        try(FileReader file = new FileReader(new File(getDataFolder(), "boxStore.json"))){
            JSONArray boxStore = (JSONArray) new JSONParser().parse(file);
            boxStore.forEach(o -> Box.fromJSON((JSONObject) o));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        Methods.consoleLog("&aPlugin enabled.");
    }

    @Override
    public void onDisable() {
        JSONArray boxStore = new JSONArray();
        Box.boxes.forEach(box -> {
            boxStore.add(box.toJSON());
            box.setLocation(null);
        });

        try (FileWriter file = new FileWriter(new File(getDataFolder(), "boxStore.json"))) {
            file.write(boxStore.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Methods.consoleLog("&cPlugin disabled.");
    }
}
