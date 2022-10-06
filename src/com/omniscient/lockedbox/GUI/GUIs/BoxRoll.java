package com.omniscient.lockedbox.GUI.GUIs;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Loot;
import com.omniscient.lockedbox.Box.Tier;
import com.omniscient.lockedbox.GUI.GUI;
import com.omniscient.lockedbox.LockedBox;
import com.omniscient.lockedbox.Utils.ItemFactory;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxRoll extends GUI {
    List<List<Integer>> SLOTS = Arrays.asList(
            Arrays.asList(22),
            Arrays.asList(21, 23),
            Arrays.asList(20, 22, 24),
            Arrays.asList(19, 21, 23, 25),
            Arrays.asList(20, 21, 22, 23, 24),
            Arrays.asList(19, 20, 21, 23, 24, 25),
            Arrays.asList(19, 20, 21, 22, 23, 24, 25),
            Arrays.asList(18, 19, 20, 21, 23, 24, 25, 26),
            Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26)
    );
    final int[] count = {10};
    private final Box box;
    private final Tier tier;
    private final List<Loot> top = new ArrayList<>();
    private final List<Loot> middle = new ArrayList<>();
    private final List<Loot> bottom = new ArrayList<>();
    public BoxRoll(Player player, Box box, Tier tier) {
        super(player);
        this.box = box;
        this.tier = tier;
        for (int i = 0; i < box.getRewardAmounts().get(tier); i++) {
            top.add(box.open(tier));
            middle.add(box.open(tier));
            bottom.add(box.open(tier));
        }
        final GUI gui = this;
        final int[] count = {10};
        new BukkitRunnable() {
            @Override
            public void run() {
                if(count[0] < 0){
                    player.closeInventory();
                    cancel();
                    for (int i = 0; i < box.getRewardAmounts().get(tier); i++) {
                        Loot reward = middle.get(i);
                        ItemStack stack = reward.getReward();
                        player.getInventory().addItem(stack);
                        player.sendMessage(Methods.color("&aYou got "+(reward.getTier() != null ? reward.getTier().getColor() : "&8")+stack.getAmount()+"x "+(reward.getDisplay().hasItemMeta() && reward.getDisplay().getItemMeta().getDisplayName() != null ? reward.getDisplay().getItemMeta().getDisplayName() : Methods.capitalize(reward.getDisplay().getType().name().replaceAll("_", " ")))+" &ain "+box.getName()+"&a."));
                    }
                    return;
                }
                bottom.clear();
                bottom.addAll(middle);
                middle.clear();
                middle.addAll(top);
                top.clear();
                for (int i = 0; i < box.getRewardAmounts().get(tier); i++) {
                    top.add(box.open(tier));
                }
                gui.update(null);
                count[0]--;
            }
        }.runTaskTimer(LockedBox.plugin, 0, 5);
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 45, Methods.stripColor(box.getName()));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemFactory.makeItem(" ", null, Material.STAINED_GLASS_PANE, box.getSkin().getDyeID()));
        }
        List<Integer> slots = SLOTS.get(box.getRewardAmounts().get(tier)-1);
        slots.forEach((slot) -> {
            inventory.setItem(slot-9, top.get(slots.indexOf(slot)).getDisplay());
            inventory.setItem(slot, middle.get(slots.indexOf(slot)).getDisplay());
            inventory.setItem(slot+9, bottom.get(slots.indexOf(slot)).getDisplay());
        });
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
    }
    @Override
    public void onOpen(InventoryOpenEvent e) {
    }
    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
