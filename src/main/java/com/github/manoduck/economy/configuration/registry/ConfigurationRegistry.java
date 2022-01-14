package com.github.manoduck.economy.configuration.registry;

import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.configuration.MessageValue;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import lombok.Data;

@Data(staticConstructor = "of")
public class ConfigurationRegistry {

    private final EconomyPlugin plugin;

    public void register() {

        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(
                plugin,
                "messages.yml"
        );

        configurationInjector.injectConfiguration(
                MessageValue.instance()
        );

        getPlugin().getLogger().info("Configurações registradas e injetadas com sucesso.");

    }
}