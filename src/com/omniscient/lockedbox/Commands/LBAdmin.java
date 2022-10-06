package com.omniscient.lockedbox.Commands;

import com.omniscient.lockedbox.GUI.GUI;
import com.omniscient.lockedbox.GUI.GUIs.BoxList;
import com.omniscient.lockedbox.Utils.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LBAdmin implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(Methods.color("&cYou must be a player to execute this command."));
            return true;
        }
        Player player = (Player) commandSender;
        GUI boxList = new BoxList(player);
        boxList.open();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
