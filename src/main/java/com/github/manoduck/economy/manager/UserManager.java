package com.github.manoduck.economy.manager;

import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.objects.User;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class UserManager {

    @Getter private final List<User> userList;
    private final EconomyPlugin plugin;

    public UserManager(EconomyPlugin plugin, List<User> userList) {

        this.plugin = plugin;
        this.userList = userList;

    }

    public void loadUser(Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> userList.add(plugin.getEconomyRepository().getUser(player.getName())));

    }

    public void unloadUser(Player player) {

        User user = findUser(player.getName());

        if (user == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (plugin.getEconomyRepository().userExists(player.getName()))
                plugin.getEconomyRepository().updateUser(user);

            else
                plugin.getEconomyRepository().createUser(user);

            userList.remove(user);

        });
    }

    public User findUser(String name) {
        return userList.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null); }
}