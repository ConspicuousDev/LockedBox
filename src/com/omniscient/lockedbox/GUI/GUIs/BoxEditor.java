package com.omniscient.lockedbox.GUI.GUIs;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Skin;
import com.omniscient.lockedbox.Box.Tier;
import com.omniscient.lockedbox.GUI.GUI;
import com.omniscient.lockedbox.Listeners.ChatListener;
import com.omniscient.lockedbox.LockedBox;
import com.omniscient.lockedbox.Utils.ItemFactory;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BoxEditor extends GUI {
    private final int PREVIEW = 10;
    private final int SKIN = 12;
    private final int LOCATION = 13;
    private final int LOOT = 14;
    private final int REWARD_AMOUNT = 15;
    private final int DELETE = 16;
    private final int BACK = 31;

    private final Box box;
    public BoxEditor(Player player, Box box) {
        super(player);
        this.box = box;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "Box Editor: "+Methods.stripColor(box.getName()));
        inventory.setItem(PREVIEW, ItemFactory.addToLore(box.makeBoxItem(), "&eClick to edit name."));
        inventory.setItem(SKIN, ItemFactory.makeItem("&eSkin", "&7Click to change the color.\n\n"+Arrays.stream(Skin.values()).map(skin -> (box.getSkin()==skin ? skin.getColor() : "&8")+"> "+Methods.capitalize(skin.name())).collect(Collectors.joining("\n")), Material.INK_SACK, box.getSkin().getDyeID()));
        inventory.setItem(LOCATION, ItemFactory.makeItem("&eLocation", "&7Left Click to set the location.\n&7Right Click to unset the location.\n&7Middle Click to rotate.\n\n"+(box.getLocation() != null ? "  &eThe box's location is currently\n  &eset to:\n    &7- World: &f"+ box.getLocation().getWorld().getName()+"\n    &7- X: &f"+ box.getLocation().getX()+"\n    &7- Y: &f"+ box.getLocation().getY()+"\n    &7- Z: &f"+box.getLocation().getZ()+"\n    &7- Rotation: &f"+Methods.capitalize(box.getRotation().name()) : "  &cThe box's location is not set."), Material.COMPASS));
        inventory.setItem(LOOT, ItemFactory.makeItem("&eLoot", "&7Click to edit the loot.", Material.DIAMOND));
        inventory.setItem(REWARD_AMOUNT, ItemFactory.makeItem("&eReward Amounts", "&7Click to edit the amount of rewards.\n\n  &fCurrent: "+ Arrays.stream(Tier.values()).map(tier -> tier.getColor()+box.getRewardAmounts().get(tier)).collect(Collectors.joining("&8-")), Material.EMERALD));
        inventory.setItem(DELETE, ItemFactory.makeItem("&cDelete", "&7Click to delete this box\n&7and its contents.\n\n  &c&lWARNING: &cThis is not reversible!", Material.BARRIER));
        inventory.setItem(BACK, ItemFactory.BACK);
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getSlot() == BACK) new BoxList(player).open();
        else if(e.getSlot() == SKIN){
            int index = Arrays.stream(Skin.values()).collect(Collectors.toList()).indexOf(box.getSkin())+1;
            box.setSkin(Skin.values()[index >= Skin.values().length ? 0 : index]);
        }else if(e.getSlot() == DELETE){
            box.setLocation(null);
            Box.boxes.remove(box);
            new BoxList(player).open();
        }else if(e.getSlot() == LOCATION){
            if(e.getAction() == InventoryAction.CLONE_STACK){
                List<BlockFace> rotations = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
                int index = rotations.indexOf(box.getRotation())+1;
                box.setRotation(rotations.get(index >= rotations.size() ? 0 : index));
            }else if(e.getAction() == InventoryAction.PICKUP_HALF) box.setLocation(null);
            else box.setLocation(player.getLocation().getBlock().getLocation());
        }else if(e.getSlot() == LOOT) new LootViewer(player, box, true).open();
        else if(e.getSlot() == PREVIEW){
            final int[] time = {15};
            ChatListener.listening.put(player, null);
            player.closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendTitle(Methods.color("&eType the new name in chat."), Methods.color("&c"+ time[0] +" second(s)"));
                    time[0]--;
                    if(ChatListener.listening.get(player) != null || time[0] < 0){
                        player.sendTitle("", "");
                        if(ChatListener.listening.get(player) != null) box.setName(ChatListener.listening.remove(player));
                        open();
                        cancel();
                    }
                }
            }.runTaskTimer(LockedBox.plugin, 0, 20);
        }else if(e.getSlot() == REWARD_AMOUNT) new RewardAmountSelector(player, box).open();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
