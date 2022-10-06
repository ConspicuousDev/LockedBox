package com.omniscient.lockedbox.GUI.GUIs;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Loot;
import com.omniscient.lockedbox.Box.Tier;
import com.omniscient.lockedbox.GUI.GUI;
import com.omniscient.lockedbox.Listeners.ChatListener;
import com.omniscient.lockedbox.LockedBox;
import com.omniscient.lockedbox.Utils.ItemFactory;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class LootViewer extends GUI {
    private final List<Integer> TIERS = Arrays.asList(1, 2, 3, 5, 6, 7);
    private final List<Integer> LOOTS = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
    private final int NOTHING = 4;
    private final int PREVIOUS = 18;
    private final int NEXT = 26;
    private final int BACK = 49;

    private final Box box;
    private final boolean admin;
    private int page = 0;
    public LootViewer(Player player, Box box, boolean admin) {
        super(player);
        this.box = box;
        this.admin = admin;
    }

    @Override
    protected Inventory getInventory() {
        int pages = (int) Math.ceil((float)box.getLoot().size()/LOOTS.size());
        Inventory inventory = Bukkit.createInventory(null, 54, "Loot "+(admin ? "Editor" : "Viewer")+": "+Methods.stripColor(box.getName()));
        if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to show the previous\n&7page. &8("+page+" out of "+pages+")", Material.ARROW));
        if(page+1 < pages) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to show the next\n&7page. &8("+(page+2)+" out of "+pages+")", Material.ARROW));
        if(admin) inventory.setItem(BACK, ItemFactory.BACK);
        for(int i = 0; i < Tier.values().length; i++){
            Tier tier = Tier.values()[i];
            int slot = TIERS.get(i);
            inventory.setItem(slot, ItemFactory.makeSkull(Methods.color(tier.getColor()+Methods.capitalize(tier.name())), "  &fChance: "+tier.getColor()+new DecimalFormat("##0.0##", DecimalFormatSymbols.getInstance(Locale.US)).format(box.getDropChance(tier)*100)+"%\n  &fItems: "+tier.getColor()+box.getLoot().stream().filter(loot -> loot.getTier() == tier).count()+(admin ? "\n\n&eClick to edit chance." : ""), tier.getTexture()));
        }
        double chance = Arrays.stream(Tier.values()).map(tier -> box.getDropChance(tier)*(box.getLoot().stream().anyMatch(loot -> loot.getTier() == tier) ? 1 : 0)).reduce(1.0, (a, b) -> a-b);
        if(chance > 0.00000001)
            inventory.setItem(NOTHING, ItemFactory.makeSkull("&4Warning", "&7There is a chance of gaining\n&cnothing &7in this box."+(admin ? "\n&7To fix this, you must add items\n&7to one or more tiers." : "")+"\n\n  &fChance: &4"+new DecimalFormat("##0.0##", DecimalFormatSymbols.getInstance(Locale.US)).format(chance*100)+"%\n    &8100%"+Arrays.stream(Tier.values()).map(tier -> "&8-"+tier.getColor()+new DecimalFormat("##0.#", DecimalFormatSymbols.getInstance(Locale.US)).format(box.getDropChance(tier)*(box.getLoot().stream().anyMatch(loot -> loot.getTier() == tier) ? 1 : 0)*100)+"%").collect(Collectors.joining("")), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5MDAzMzliYjk3MWYwMjhhZmU2NzI0YTg1MjE5YmEyMzM5OTE4YmE0OWMxMTlkMmZiODcxZTQ3YWM5OWIzOSJ9fX0="));
        for (int i = page*LOOTS.size(); i < Math.min((page+1)*LOOTS.size(), box.getLoot().size()); i++) {
            Loot loot = box.getLoot().get(i);
            int slot = LOOTS.get(i-page* LOOTS.size());
            inventory.setItem(slot, ItemFactory.addToLore(loot.getDisplay(), "\n  &fTier: "+loot.getTier().getColor()+Methods.capitalize(loot.getTier().name())+"\n  &fChance: "+loot.getTier().getColor()+new DecimalFormat("##0.0##", DecimalFormatSymbols.getInstance(Locale.US)).format((box.getDropChance(loot.getTier())*100)/box.getLoot().stream().filter(l -> loot.getTier() == l.getTier()).count())+"%"+(admin ? "\n\n&eClick to change tier.\n&eShift+Click to remove." : "")));
        }
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        int pages = (int) Math.ceil((float)box.getLoot().size()/LOOTS.size());
        if(e.getSlot() == PREVIOUS && page > 0) page--;
        else if(e.getSlot() == NEXT && page+1 < pages) page++;
        if(!admin) return;
        if(e.getSlot() == BACK) new BoxEditor(player, box).open();
        else if(e.getClickedInventory() == player.getOpenInventory().getBottomInventory()){
            if(e.getCurrentItem().getType() == Material.AIR) return;
            box.addLoot(e.getCurrentItem().clone(), Tier.COMMON);
        }else if(LOOTS.contains(e.getSlot())){
            if(e.getCurrentItem().getType() == Material.AIR) return;
            Loot loot = box.getLoot().get(page*LOOTS.size()+LOOTS.indexOf(e.getSlot()));
            if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
                box.getLoot().remove(loot);
            else{
                int index = Arrays.stream(Tier.values()).collect(Collectors.toList()).indexOf(loot.getTier())+1;
                loot.setTier(Tier.values()[index >= Tier.values().length ? 0 : index]);
            }
        }else if(TIERS.contains(e.getSlot())){
            Tier tier = Tier.values()[TIERS.indexOf(e.getSlot())];
            final int[] time = {15};
            ChatListener.listening.put(player, null);
            player.closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendTitle(Methods.color("&eType chance for "+tier.getColor()+Methods.capitalize(tier.name())+" &etier."), Methods.color("&c"+ time[0] +" second(s)"));
                    time[0]--;
                    if(ChatListener.listening.get(player) != null || time[0] < 0){
                        player.sendTitle("", "");
                        if(ChatListener.listening.get(player) != null) {
                            try {
                                double chance = Double.parseDouble(ChatListener.listening.remove(player));
                                if(chance < 0)
                                    player.sendMessage(Methods.color("&cThe chance must be positive."));
                                else if(Arrays.stream(Tier.values()).map(t -> t != tier ? box.getDropChance(t) : chance/100).reduce(0.0, Double::sum) > 1)
                                    player.sendMessage(Methods.color("&cThe total chance must not exceed 100%."));
                                else box.setDropChance(tier, chance / 100);
                            }catch (NumberFormatException e){
                                player.sendMessage(Methods.color("&cYou can only input numbers (&7'10.3' &cfor &710.3% &cchance)."));
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
