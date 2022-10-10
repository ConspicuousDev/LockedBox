package com.omniscient.lockedbox.Listeners;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Tier;
import com.omniscient.lockedbox.GUI.GUIs.BoxRoll;
import com.omniscient.lockedbox.GUI.GUIs.LootViewer;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BoxListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Block b = e.getClickedBlock();
        if(b == null || b.getType() != Material.CHEST) return;
        Location location = b.getLocation();
        Box box = Box.getByLocation(location);
        if(box == null) return;
        e.setCancelled(true);
        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            new LootViewer(player, box, false).open();
        }else if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR){
                player.sendMessage(Methods.color("&cYou need a key for this."));
                return;
            }
            if(!player.getItemInHand().hasItemMeta()){
                player.sendMessage(Methods.color("&cYou need a key for this."));
                return;
            }
            for(Tier tier : Tier.values()) {
                ItemStack key = box.makeKey(tier, 1);
                ItemStack hand = player.getItemInHand().clone();
                hand.setAmount(1);
                if(hand.equals(key)){
                    new BoxRoll(e.getPlayer(), box, tier).open();
                    ItemStack item = player.getItemInHand();
                    if(item.getAmount() > 1) item.setAmount(item.getAmount()-1);
                    else player.getInventory().setItemInHand(null);
                    return;
                }
            }
            player.sendMessage(Methods.color("&cYou need a key for this."));
        }
    }
}
