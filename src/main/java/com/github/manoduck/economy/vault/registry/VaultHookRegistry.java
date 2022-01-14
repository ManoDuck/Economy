package com.github.manoduck.economy.vault.registry;

import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.vault.VaultEconomyHook;
import lombok.Data;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

@Data(staticConstructor = "of")
public class VaultHookRegistry {

    private final EconomyPlugin plugin;

    public VaultHookRegistry register() {

        Bukkit.getServer().getServicesManager()
                .register(
                        Economy.class,
                        new VaultEconomyHook(),
                        plugin,
                        ServicePriority.Highest
                );

        getPlugin().getLogger().info("Associação com o 'Vault' realizada com sucesso.");

        return this;

    }
}