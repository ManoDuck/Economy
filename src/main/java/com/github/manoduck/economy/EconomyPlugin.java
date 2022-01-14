package com.github.manoduck.economy;

import com.github.manoduck.economy.command.MoneyCommand;
import com.github.manoduck.economy.configuration.registry.ConfigurationRegistry;
import com.github.manoduck.economy.listener.UserListeners;
import com.github.manoduck.economy.manager.BalanceTopManager;
import com.github.manoduck.economy.manager.UserManager;
import com.github.manoduck.economy.storage.DatabaseConnector;
import com.github.manoduck.economy.storage.EconomyRepository;
import com.github.manoduck.economy.vault.registry.VaultHookRegistry;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPlugin extends JavaPlugin {

    @Getter private static EconomyPlugin instance;

    @Getter private DatabaseConnector databaseConnector;
    @Getter private EconomyRepository economyRepository;

    @Getter private UserManager userManager;
    @Getter private BalanceTopManager balanceTopManager;

    @Override
    public void onLoad() { instance = this; saveDefaultConfig(); }

    @Override
    public void onEnable() {

        getLogger().info("Iniciando o carregamento do plugin!");

        databaseConnector = new DatabaseConnector(
                getConfig().getString("MySQL.url"),
                getConfig().getString("MySQL.username"),
                getConfig().getString("MySQL.password")
        );

        economyRepository = new EconomyRepository(databaseConnector.dataSource);

        balanceTopManager = new BalanceTopManager(this);
        userManager = new UserManager(this, Lists.newArrayList());

        ConfigurationRegistry.of(this).register();
        VaultHookRegistry.of(this).register();

        getCommand("money").setExecutor(new MoneyCommand());

        Bukkit.getPluginManager().registerEvents(new UserListeners(), this);

        Bukkit.getOnlinePlayers()
                .forEach(player -> userManager.loadUser(player));

    }

    @Override
    public void onDisable() {}

}