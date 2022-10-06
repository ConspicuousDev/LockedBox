package com.omniscient.lockedbox.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ChatListener implements Listener {
    public static final Map<Player, String> listening = new HashMap<>();

    @EventHandler
    public void onPlayerSendMessage(AsyncPlayerChatEvent e){
        if(!listening.containsKey(e.getPlayer())) return;
        listening.put(e.getPlayer(), e.getMessage().trim());
        e.setCancelled(true);
    }
}
