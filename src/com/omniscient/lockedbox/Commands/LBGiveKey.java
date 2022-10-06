package com.omniscient.lockedbox.Commands;

import com.omniscient.lockedbox.Box.Box;
import com.omniscient.lockedbox.Box.Tier;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LBGiveKey implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length < 3){
            commandSender.sendMessage(Methods.color("&cPlease use: "+command.getUsage()));
            return true;
        }
        Player target = Bukkit.getPlayer(strings[0]);
        if(target == null){
            commandSender.sendMessage(Methods.color("&cThe player &7"+strings[0]+" &cwas not found."));
            return true;
        }
        int amount;
        try{
            amount = Integer.parseInt(strings[1]);
        }catch (NumberFormatException e){
            commandSender.sendMessage(Methods.color("&cThe amount must be a number."));
            return true;
        }
        if(amount <= 0){
            commandSender.sendMessage(Methods.color("&cThe amount must be a number greater than 0."));
            return true;
        }
        Tier tier;
        try {
            tier = Tier.valueOf(strings[2].toUpperCase());
        }catch (IllegalArgumentException e){
            commandSender.sendMessage(Methods.color("&cThe tier &7"+strings[2]+" &cwas not found."));
            return true;
        }
        String boxName = Arrays.stream(Arrays.copyOfRange(strings, 3, strings.length)).collect(Collectors.joining(" "));
        Box box = Box.getByName(boxName);
        if(box == null){
            commandSender.sendMessage(Methods.color("&cThe box &7"+boxName+" &cwas not found."));
            return true;
        }
        ItemStack key = box.makeKey(tier, amount);
        commandSender.sendMessage(Methods.color("&aThe player &7"+target.getName()+" &ahas received &e"+amount+"x "+key.getItemMeta().getDisplayName()+"&a."));
        target.getInventory().addItem(key);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();
        if(strings.length == 1)
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        else if(strings.length == 3)
            completions.addAll(Arrays.stream(Tier.values()).map(Tier::name).collect(Collectors.toList()));
        else if(strings.length == 4)
            completions.addAll(Box.boxes.stream().map(box -> Methods.stripColor(box.getName())).collect(Collectors.toList()));
        return completions;
    }
}
