package com.omniscient.lockedbox.Box;

import com.omniscient.lockedbox.Utils.HologramFactory;
import com.omniscient.lockedbox.Utils.ItemFactory;
import com.omniscient.lockedbox.Utils.ItemStackSerialization;
import com.omniscient.lockedbox.Utils.Methods;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Chest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Box {
    public static final List<Box> boxes = new ArrayList<>();

    private final UUID uuid;
    private String name = "&7New Locked Box";
    private Location location = null;
    private BlockFace rotation = BlockFace.NORTH;
    private Skin skin = Skin.CYAN;
    private Map<Tier, Integer> rewardAmounts = new HashMap<Tier, Integer>(){{
        put(Tier.COMMON, 1);
        put(Tier.UNCOMMON, 1);
        put(Tier.ELITE, 1);
        put(Tier.RARE, 1);
        put(Tier.LEGENDARY, 1);
        put(Tier.MYSTIC, 1);
    }};
    private final List<Loot> loot = new ArrayList<>();
    private final Map<Tier, Double> dropChance = new HashMap<Tier, Double>(){{
        put(Tier.COMMON, .60);
        put(Tier.UNCOMMON, .23);
        put(Tier.ELITE, .105);
        put(Tier.RARE, .05);
        put(Tier.LEGENDARY, .01);
        put(Tier.MYSTIC, .005);
    }};
    private final List<ArmorStand> armorStands = new ArrayList<>();
    public Box(UUID uuid){
        this.uuid = uuid;
        boxes.add(this);
    }

    public UUID getUUID() {
        return uuid;
    }
    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }
    public BlockFace getRotation() {
        return rotation;
    }
    public Skin getSkin() {
        return skin;
    }
    public Map<Tier, Integer> getRewardAmounts() {
        return rewardAmounts;
    }
    public List<Loot> getLoot() {
        return loot;
    }
    public double getDropChance(Tier tier){
        return dropChance.get(tier);
    }
    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public void setName(String name){
        this.name = (name.startsWith("&7") ? "" : "&7")+name;
        setLocation(location);
    }
    public void setLocation(Location location) {
        if(this.location != null) {
            armorStands.forEach(Entity::remove);
            this.location.getWorld().getBlockAt(this.location).setType(Material.AIR);
        }
        if(location != null) {
            armorStands.add(HologramFactory.makeHologram(location.clone().add(.5, 1.3, .5), name));
            armorStands.add(HologramFactory.makeHologram(location.clone().add(.5, 1.0, .5), "&eLeft Click to view rewards."));
            armorStands.add(HologramFactory.makeHologram(location.clone().add(.5, .7, .5), "&eRight Click with key to open."));
            location.getBlock().setType(Material.CHEST);
            BlockState state = location.getBlock().getState();
            Chest chest = new Chest(rotation);
            state.setData(chest);
            state.update();
            CraftWorld world = (CraftWorld) location.getWorld();
            TileEntityChest tileEntity = (TileEntityChest) world.getTileEntityAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            tileEntity.a(Methods.stripColor(name));
        }
        this.location = location;
    }
    public void setRotation(BlockFace rotation) {
        this.rotation = rotation;
        setLocation(location);
    }
    public void setSkin(Skin skin) {
        this.skin = skin;
    }
    public void setRewardAmount(Tier tier, int amount) {
        this.rewardAmounts.put(tier, amount);
    }
    public void addLoot(ItemStack stack, Tier tier){
        this.loot.add(new Loot(stack, tier));
    }
    public void setDropChance(Tier tier, double chance){
        this.dropChance.put(tier, chance);
    }

    public Loot open(Tier minTier){
        double r = Math.random();
        double c = 0.0;
        double t = 0;
        double ct = 0;
        boolean high = false;
        for (Tier tier : Tier.values()) {
            t += getDropChance(tier);
            if (tier == minTier) high = true;
            if (!high) continue;
            ct += getDropChance(tier);
        }
        double multiplier = t/ct;
        high = false;
        Tier selected = null;
        for (Tier tier : Tier.values()) {
            if(tier == minTier) high = true;
            if(!high) continue;
            if(r < getDropChance(tier)*multiplier+c) {
                selected = tier;
                break;
            }
            c += getDropChance(tier)*multiplier;
        }
        Tier finalSelected = selected;
        List<Loot> tierRewards = loot.stream().filter(l -> l.getTier() == finalSelected).collect(Collectors.toList());
        Loot reward;
        if(tierRewards.size() == 0 || finalSelected == null)
            reward = Loot.NOTHING;
        else
            reward = tierRewards.get(new Random().nextInt(tierRewards.size()));
        return reward;
    }
    public ItemStack makeKey(Tier tier, int amount){
        ItemStack key = ItemFactory.makeItem("&7"+name+tier.getColor()+" Key", "&7Use this to open a\n"+name+" &7box.\n\n  &fTier: "+tier.getColor()+Methods.capitalize(tier.name()), Material.TRIPWIRE_HOOK);
        key.setAmount(amount);
        return key;
    }
    public ItemStack makeBoxItem(){
        return ItemFactory.makeSkull(
                "&7"+name,
                "",
                skin.getTexture()
        );
    }

    public JSONObject toJSON(){
        JSONObject boxObject = new JSONObject();
        boxObject.put("uuid", uuid.toString());
        boxObject.put("name", name);
        if(location != null) {
            JSONObject locationObject = new JSONObject();
            locationObject.put("world", location.getWorld().getName());
            locationObject.put("x", location.getBlockX());
            locationObject.put("y", location.getBlockY());
            locationObject.put("z", location.getBlockZ());
            boxObject.put("location", locationObject);
        }else{
            boxObject.put("location", null);
        }
        boxObject.put("rotation", rotation.name());
        boxObject.put("skin", skin.name());
        JSONObject rewardAmountsObject = new JSONObject();
        rewardAmounts.keySet().forEach(tier -> rewardAmountsObject.put(tier.name(), rewardAmounts.get(tier)));
        boxObject.put("rewardAmounts", rewardAmountsObject);
        JSONArray lootArray = new JSONArray();
        loot.forEach(loot -> {
            JSONObject lootObject = new JSONObject();
            lootObject.put("display", ItemStackSerialization.itemStackArrayToBase64(new ItemStack[]{loot.getDisplay()}));
            lootObject.put("reward", ItemStackSerialization.itemStackArrayToBase64(new ItemStack[]{loot.getReward()}));
            lootObject.put("tier", loot.getTier().name());
            lootArray.add(lootObject);
        });
        boxObject.put("loot", lootArray);
        JSONObject dropChanceObject = new JSONObject();
        dropChance.keySet().forEach(tier -> dropChanceObject.put(tier.name(), dropChance.get(tier)));
        boxObject.put("dropChance", dropChanceObject);
        return boxObject;
    }
    public static Box fromJSON(JSONObject boxObject){
        Box box = new Box(UUID.fromString((String) boxObject.get("uuid")));
        if(boxObject.containsKey("name")) box.setName((String) boxObject.get("name"));
        if (boxObject.containsKey("location") && boxObject.get("location") != null) {
            JSONObject locationObject = (JSONObject) boxObject.get("location");
            box.setLocation(new Location(
                    Bukkit.getWorld((String) locationObject.get("world")),
                    (long) locationObject.get("x"),
                    (long) locationObject.get("y"),
                    (long) locationObject.get("z")
            ));
        } else {
            box.setLocation(null);
        }
        if(boxObject.containsKey("rotation")) box.setRotation(BlockFace.valueOf((String) boxObject.get("rotation")));
        if(boxObject.containsKey("skin")) box.setSkin(Skin.valueOf((String) boxObject.get("skin")));
        if(boxObject.containsKey("rewardAmounts")){
            JSONObject rewardAmountsObject = (JSONObject) boxObject.get("rewardAmounts");
            rewardAmountsObject.keySet().forEach(key -> box.setRewardAmount(Tier.valueOf((String) key), Math.toIntExact((Long) rewardAmountsObject.get(key))));
        }
        if(boxObject.containsKey("loot")) {
            JSONArray lootArray = (JSONArray) boxObject.get("loot");
            lootArray.forEach(o -> {
                JSONObject lootObject = (JSONObject) o;
                try {
                    Loot loot = new Loot(
                            ItemStackSerialization.itemStackArrayFromBase64((String) lootObject.get("display"))[0],
                            ItemStackSerialization.itemStackArrayFromBase64((String) lootObject.get("reward"))[0],
                            Tier.valueOf((String) lootObject.get("tier"))
                    );
                    box.getLoot().add(loot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        if(boxObject.containsKey("dropChance")) {
            JSONObject dropChanceObject = (JSONObject) boxObject.get("dropChance");
            dropChanceObject.keySet().forEach(key -> box.setDropChance(Tier.valueOf((String) key), (double) dropChanceObject.get(key)));
        }
        return box;
    }

    public static Box getByLocation(Location location){
        return Box.boxes.stream()
                .filter(box -> box.getLocation().equals(location))
                .findFirst()
                .orElse(null);
    }
    public static Box getByName(String name){
        return boxes.stream()
                .filter(box -> Methods.stripColor(box.getName()).equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
