package com.omniscient.lockedbox.Box;

import com.omniscient.lockedbox.Utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Loot {
    public static final Loot NOTHING = new Loot(ItemFactory.makeItem("&cNothing", null, Material.BARRIER), new ItemStack(Material.AIR, 1), null);

    private ItemStack display;
    private ItemStack reward;
    private Tier tier;
    public Loot(ItemStack stack, Tier tier){
        this.display = stack;
        this.reward = stack;
        this.tier = tier;
    }
    public Loot(ItemStack display, ItemStack reward, Tier tier){
        this.display = display;
        this.reward = reward;
        this.tier = tier;
    }

    public ItemStack getDisplay() {
        return display;
    }
    public ItemStack getReward() {
        return reward;
    }
    public Tier getTier() {
        return tier;
    }

    public void setDisplay(ItemStack display) {
        this.display = display;
    }
    public void setReward(ItemStack reward) {
        this.reward = reward;
    }
    public void setTier(Tier tier) {
        this.tier = tier;
    }
}
