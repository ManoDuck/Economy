package com.github.manoduck.economy.manager;

import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.objects.BalanceTop;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;

public class BalanceTopManager {

    @Getter private List<BalanceTop> balanceTopList;

    private String topPlayer;

    public BalanceTopManager(EconomyPlugin plugin) {

        balanceTopList = Lists.newArrayList();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () ->
                balanceTopList = plugin.getEconomyRepository().getBalanceTop(), 0L, 20L*60*5);

    }

    public BalanceTop getTycoon() { return balanceTopList.get(0); }

    public boolean isTycoon(String name) {

        if (getTycoon() == null) return false;

        return getTycoon().getName().equalsIgnoreCase(name);

    }
}