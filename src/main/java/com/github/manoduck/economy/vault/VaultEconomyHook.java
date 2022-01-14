package com.github.manoduck.economy.vault;

import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.objects.User;
import com.github.manoduck.economy.utils.NumberFormatter;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VaultEconomyHook extends EconomyWrapper {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "Economy";
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return NumberFormatter.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "coins";
    }

    @Override
    public String currencyNameSingular() {
        return "coin";
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return EconomyPlugin.getInstance().getUserManager().findUser(player.getName()) != null;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        User user = EconomyPlugin.getInstance().getUserManager().findUser(player.getName());
        return user == null ? 0 : user.getBalance();
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {

        if (NumberFormatter.isInvalid(amount)) return false;

        User user = EconomyPlugin.getInstance().getUserManager().findUser(player.getName());
        if (user == null) return false;

        return user.hasBalance(amount);

    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double initialAmount) {

        if (initialAmount == 0 || NumberFormatter.isInvalid(initialAmount)) {
            return new EconomyResponse(initialAmount, 0, EconomyResponse.ResponseType.FAILURE, "Valor inválido");
        }

        User user = EconomyPlugin.getInstance().getUserManager().findUser(player.getName());

        if (user != null) {

            Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(), () -> {

                user.withdrawBalance(initialAmount);
                EconomyPlugin.getInstance().getEconomyRepository().setBalance(user.getName(), user.getBalance());

            });

        }

        return new EconomyResponse(
                initialAmount,
                0,
                EconomyResponse.ResponseType.FAILURE,
                "Conta inválida."
        );
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double initialAmount) {

        if (initialAmount == 0 || NumberFormatter.isInvalid(initialAmount)) {
            return new EconomyResponse(initialAmount, 0, EconomyResponse.ResponseType.FAILURE, "Valor inválido");
        }

        User user = EconomyPlugin.getInstance().getUserManager().findUser(player.getName());

        if (user != null) {

            Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(), () -> {

                user.depositBalance(initialAmount);
                EconomyPlugin.getInstance().getEconomyRepository().setBalance(user.getName(), user.getBalance());

            });

        }

        return new EconomyResponse(
                initialAmount,
                0,
                EconomyResponse.ResponseType.FAILURE,
                "Conta inválida"
        );
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {

        User user = EconomyPlugin.getInstance().getUserManager().findUser(player.getName());

        if (user == null) return false;

        Player player1 = (Player) player;

        EconomyPlugin.getInstance().getUserManager().loadUser(player1);

        return true;

    }
}