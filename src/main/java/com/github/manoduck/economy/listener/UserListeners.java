package com.github.manoduck.economy.listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.configuration.MessageValue;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListeners implements Listener {

    @EventHandler
    void onJoin(PlayerJoinEvent event) {

        EconomyPlugin.getInstance().getUserManager().loadUser(event.getPlayer());

    }

    @EventHandler
    void onQuit(PlayerQuitEvent event) {

        EconomyPlugin.getInstance().getUserManager().unloadUser(event.getPlayer());

    }

    @EventHandler
    void onChat(ChatMessageEvent event) {

        if (EconomyPlugin.getInstance().getBalanceTopManager().isTycoon(event.getSender().getName())) {

            event.setTagValue("tycoon", MessageValue.get(MessageValue::tycoonTag));

        }
    }
}