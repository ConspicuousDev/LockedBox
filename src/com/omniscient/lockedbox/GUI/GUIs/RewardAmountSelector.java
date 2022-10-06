package com.omniscient.lockedbox.GUI.GUIs;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Tier;
import com.omniscient.lockedbox.GUI.GUI;
import com.omniscient.lockedbox.Listeners.ChatListener;
import com.omniscient.lockedbox.LockedBox;
import com.omniscient.lockedbox.Utils.ItemFactory;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RewardAmountSelector extends GUI {
    private final List<Integer> TIERS = Arrays.asList(10, 11, 12, 14, 15, 16);
    private final int BACK = 31;

    private final Box box;
    public RewardAmountSelector(Player player, Box box) {
        super(player);
        this.box = box;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "Box Editor: "+ Methods.stripColor(box.getName()));
        for(int i = 0; i < Tier.values().length; i++){
            Tier tier = Tier.values()[i];
            int slot = TIERS.get(i);
            inventory.setItem(slot, ItemFactory.makeSkull(Methods.color(tier.getColor()+Methods.capitalize(tier.name())), "&eClick to set tier reward amount. \n\n  &fReward Amount: "+tier.getColor()+box.getRewardAmounts().get(tier), tier.getTexture()));
        }
        inventory.setItem(BACK, ItemFactory.BACK);
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getSlot() == BACK) new BoxEditor(player, box).open();
        else if(TIERS.contains(e.getSlot())){
            Tier tier = Tier.values()[TIERS.indexOf(e.getSlot())];
            final int[] time = {15};
            ChatListener.listening.put(player, null);
            player.closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendTitle(Methods.color("&eType reward amount for "+box.getName()+" &ebox."), Methods.color("&c"+ time[0] +" second(s)"));
                    time[0]--;
                    if(ChatListener.listening.get(player) != null || time[0] < 0){
                        player.sendTitle("", "");
                        if(ChatListener.listening.get(player) != null) {
                            try {
                                int amount = Integer.parseInt(ChatListener.listening.remove(player));
                                if(amount < 1)
                                    player.sendMessage(Methods.color("&cThe minimum amount of rewards is 1."));
                                else if(amount > 9)
                                    player.sendMessage(Methods.color("&cThe maximum amount of rewards is 9."));
                                else box.setRewardAmount(tier, amount);
                            }catch (NumberFormatException e){
                                player.sendMessage(Methods.color("&cYou can only input numbers."));
                            }
                        }
                        open();
                        cancel();
                    }
                }
            }.runTaskTimer(LockedBox.plugin, 0, 20);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
    }
    @Override
    public void onClose(InventoryCloseEvent e) {
    }
}
