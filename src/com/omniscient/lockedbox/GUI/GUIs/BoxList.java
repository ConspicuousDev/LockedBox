package com.omniscient.lockedbox.GUI.GUIs;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.GUI.GUI;
import com.omniscient.lockedbox.Utils.ItemFactory;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BoxList extends GUI {
    private final List<Integer> BOXES = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    private final int PREVIOUS = 18;
    private final int NEXT = 26;
    private final int NEW = 49;

    private int page = 0;
    public BoxList(Player player) {
        super(player);
    }

    @Override
    protected Inventory getInventory() {
        int pages = (int) Math.ceil((float)Box.boxes.size()/BOXES.size());
        Inventory inventory = Bukkit.createInventory(null, 54, "Box Editor");
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to show the previous\n&7page. &8("+page+" out of "+pages+")", Material.ARROW));
        if(page+1 < pages) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to show the next\n&7page. &8("+(page+2)+" out of "+pages+")", Material.ARROW));
        inventory.setItem(NEW, ItemFactory.makeItem("&aCreate new box", "&7Click to create and\n&7configure a new box.", Material.INK_SACK, 10));
        for (int i = page*BOXES.size(); i < Math.min((page+1)*BOXES.size(), Box.boxes.size()); i++) {
            Box box = Box.boxes.get(i);
            int slot = BOXES.get(i-page*BOXES.size());
            inventory.setItem(slot, ItemFactory.addToLore(box.makeBoxItem(), "&eClick to edit box."));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int pages = (int) Math.ceil((float)Box.boxes.size()/BOXES.size());
        if(e.getSlot() == PREVIOUS && page > 0) page--;
        else if(e.getSlot() == NEXT && page+1 < pages) page++;
        else if(e.getSlot() == NEW) new BoxEditor(player, new Box(UUID.randomUUID())).open();
        else if(BOXES.contains(e.getSlot())){
            if(e.getCurrentItem().getType() == Material.AIR) return;
            new BoxEditor(player, Box.boxes.get(page*BOXES.size()+BOXES.indexOf(e.getSlot()))).open();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
