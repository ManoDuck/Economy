package com.github.manoduck.economy.command;

import com.github.manoduck.economy.EconomyPlugin;
import com.github.manoduck.economy.configuration.MessageValue;
import com.github.manoduck.economy.objects.BalanceTop;
import com.github.manoduck.economy.objects.User;
import com.github.manoduck.economy.utils.Helper;
import com.github.manoduck.economy.utils.NumberFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MoneyCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length == 0) {

            if (!(commandSender instanceof Player)) {

                commandSender.sendMessage(MessageValue.get(MessageValue::incorrectTarget));
                return false;

            }

            User user = EconomyPlugin.getInstance().getUserManager().findUser(commandSender.getName());

            if (user == null) {

                commandSender.sendMessage("§cVocê não está em nosso banco de dados, relogue e tente novamente.");
                return false;

            }

            commandSender.sendMessage(MessageValue.get(MessageValue::playerBalance)
                    .replace("{balance}", NumberFormatter.format(user.getBalance()))
            );

            return true;

        }

        if (strings.length == 1) {

            String targetName = strings[0];

            if (targetName.equalsIgnoreCase("top")
                    || targetName.equalsIgnoreCase("ranking")
                    || targetName.equalsIgnoreCase("rank")
                    || targetName.equalsIgnoreCase("ricos")
                    || targetName.equalsIgnoreCase("magnatas")) {

                List<BalanceTop> balanceTopList = EconomyPlugin.getInstance().getBalanceTopManager().getBalanceTopList();

                if (balanceTopList.isEmpty()) {

                    commandSender.sendMessage(MessageValue.get(MessageValue::emptyTop));
                    return false;

                }

                StringBuilder message = new StringBuilder();
                AtomicInteger count = new AtomicInteger(0);

                message.append("\n")
                        .append(MessageValue.get(MessageValue::moneyTopHeader))
                        .append("\n \n§f");

                balanceTopList.forEach(balanceTop -> message.append(String.format("%sº %s§f: (%s)\n",
                        count.incrementAndGet(),
                        (count.get() == 1 ? MessageValue.get(MessageValue::tycoonTag) : "") + balanceTop.getName(),
                        NumberFormatter.format(balanceTop.getBalance()))));

                message.append("\n \n§f");
                message.append(MessageValue.get(MessageValue::moneyTopFooter));
                message.append("\n \n§f");

                commandSender.sendMessage(message.toString());

                return false;

            }

            if (targetName.equalsIgnoreCase("ajuda")
                    || targetName.equalsIgnoreCase("help")) {

                for (String string : MessageValue.get(MessageValue::moneyHelp)) { commandSender.sendMessage(string); }

                return true;

            }

            User targetUser = EconomyPlugin.getInstance().getUserManager().findUser(targetName);

            if (targetUser == null) {

                Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(), () -> {

                    if (!EconomyPlugin.getInstance().getEconomyRepository().userExists(targetName)) {

                        commandSender.sendMessage("§cEste jogador não está em nosso banco de dados.");

                        return;

                    }

                    commandSender.sendMessage(MessageValue.get(MessageValue::targetBalance)
                            .replace("{player}", targetName)
                            .replace("{balance}", NumberFormatter.format(EconomyPlugin.getInstance().getEconomyRepository().getBalance(targetName)))
                    );

                });

                return false;

            }

            commandSender.sendMessage(MessageValue.get(MessageValue::targetBalance)
                    .replace("{player}", targetUser.getName())
                    .replace("{balance}", NumberFormatter.format(targetUser.getBalance()))
            );

            return true;

        }

        if (strings.length == 3) {

            String firstArgument = strings[0];

            if (firstArgument.equalsIgnoreCase("enviar")
                    || firstArgument.equalsIgnoreCase("pay")
                    || firstArgument.equalsIgnoreCase("pagar")) {

                if (!(commandSender instanceof Player)) {

                    commandSender.sendMessage(MessageValue.get(MessageValue::incorrectTarget));
                    return false;

                }

                User playerUser = EconomyPlugin.getInstance().getUserManager().findUser(commandSender.getName());

                if (playerUser == null) {

                    commandSender.sendMessage("§cVocê não está em nosso banco de dados, relogue e tente novamente.");
                    return false;

                }

                if (strings[2].contains(".")) {

                    commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                    return false;

                }

                Double balance = Helper.tryParseDouble(strings[2]);

                if (balance == null
                        || balance.isNaN()
                        || balance.isInfinite()
                        || balance < 1.0) {

                    commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                    return false;

                }

                if (balance > playerUser.getBalance()) {

                    commandSender.sendMessage("§cVocê não possui money suficiente para executar esta ação.");
                    return true;

                }

                String targetName = strings[1];
                User targetUser = EconomyPlugin.getInstance().getUserManager().findUser(targetName);

                if (targetName.equals(commandSender.getName())) {

                    commandSender.sendMessage("§cVocê não pode enviar money para si mesmo.");
                    return false;

                }

                if (targetUser == null) {

                    Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(),() -> {

                        if (!EconomyPlugin.getInstance().getEconomyRepository().userExists(targetName)) {

                            commandSender.sendMessage("§cEste jogador não está em nosso banco de dados.");

                            return;

                        }

                        playerUser.withdrawBalance(balance);
                        EconomyPlugin.getInstance().getEconomyRepository().addBalance(targetName, balance);

                    });

                    commandSender.sendMessage(MessageValue.get(MessageValue::moneyPay)
                            .replace("{player}", targetName)
                            .replace("{balance}", NumberFormatter.format(balance)));

                    return true;

                }

                Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(), () -> {

                    playerUser.withdrawBalance(balance);
                    targetUser.depositBalance(balance);
                    EconomyPlugin.getInstance().getEconomyRepository().setBalance(playerUser.getName(), playerUser.getBalance());
                    EconomyPlugin.getInstance().getEconomyRepository().setBalance(targetUser.getName(), targetUser.getBalance());

                });

                commandSender.sendMessage(MessageValue.get(MessageValue::moneyPay)
                        .replace("{player}", targetUser.getName())
                        .replace("{balance}", NumberFormatter.format(balance)));

                targetUser.getPlayer().sendMessage(MessageValue.get(MessageValue::moneyReceive)
                        .replace("{player}", playerUser.getName())
                        .replace("{balance}", NumberFormatter.format(balance)));

                return true;

            }

            if (firstArgument.equalsIgnoreCase("definir")
                    || firstArgument.equalsIgnoreCase("define")
                    || firstArgument.equalsIgnoreCase("setar")
                    || firstArgument.equalsIgnoreCase("set")) {

                if (!commandSender.hasPermission("economy.money.set")) {

                    commandSender.sendMessage(MessageValue.get(MessageValue::noPermission));
                    return false;

                }

                if (strings[2].contains(".")) {

                    commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                    return false;

                }

                Double balance = Helper.tryParseDouble(strings[2]);

                if (balance == null
                        || balance.isNaN()
                        || balance.isInfinite()) {

                    commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                    return false;

                }

                String targetName = strings[1];
                User targetUser = EconomyPlugin.getInstance().getUserManager().findUser(targetName);

                if (targetUser == null) {

                    Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(),() -> {

                        if (!EconomyPlugin.getInstance().getEconomyRepository().userExists(targetName)) {

                            commandSender.sendMessage("§cEste jogador não está em nosso banco de dados.");
                            return;

                        }

                        EconomyPlugin.getInstance().getEconomyRepository().setBalance(targetName, balance);

                    });

                    commandSender.sendMessage(MessageValue.get(MessageValue::moneySet)
                            .replace("{player}", targetName)
                            .replace("{balance}", NumberFormatter.format(balance)));

                    return true;

                }

                Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(), () -> {

                    targetUser.setBalance(balance);
                    EconomyPlugin.getInstance().getEconomyRepository().setBalance(targetUser.getName(), targetUser.getBalance());

                });

                commandSender.sendMessage(MessageValue.get(MessageValue::moneySet)
                        .replace("{player}", targetName)
                        .replace("{balance}", NumberFormatter.format(balance)));

                return true;

            }

            if (firstArgument.equalsIgnoreCase("adicionar")
                    || firstArgument.equalsIgnoreCase("add")) {

                if (!commandSender.hasPermission("economy.money.add")) {

                    commandSender.sendMessage(MessageValue.get(MessageValue::noPermission));
                    return false;

                }

                if (strings[2].contains(".")) {

                    commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                    return false;

                }

                Double balance = Helper.tryParseDouble(strings[2]);

                if (balance == null
                        || balance.isNaN()
                        || balance.isInfinite()) {

                    commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                    return false;

                }

                String targetName = strings[1];
                User targetUser = EconomyPlugin.getInstance().getUserManager().findUser(targetName);

                if (targetUser == null) {

                    Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(),() -> {

                        if (!EconomyPlugin.getInstance().getEconomyRepository().userExists(targetName)) {

                            commandSender.sendMessage("§cEste jogador não está em nosso banco de dados.");
                            return;

                        }

                        EconomyPlugin.getInstance().getEconomyRepository().addBalance(targetName, balance);

                    });

                    commandSender.sendMessage(MessageValue.get(MessageValue::moneyAdd)
                            .replace("{player}", targetName)
                            .replace("{balance}", NumberFormatter.format(balance)));

                    return true;

                }

                Bukkit.getScheduler().runTaskAsynchronously(EconomyPlugin.getInstance(), () -> {

                    targetUser.depositBalance(balance);
                    EconomyPlugin.getInstance().getEconomyRepository().setBalance(targetUser.getName(), targetUser.getBalance());

                });

                commandSender.sendMessage(MessageValue.get(MessageValue::moneyAdd)
                        .replace("{player}", targetName)
                        .replace("{balance}", NumberFormatter.format(balance)));

                return true;

            }
        }

        return false;

    }
}