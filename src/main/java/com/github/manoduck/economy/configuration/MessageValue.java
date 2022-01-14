package com.github.manoduck.economy.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigSection("messages")
@ConfigFile("messages.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageValue implements ConfigurationInjectable {

    @Getter private static final MessageValue instance = new MessageValue();

    // Mensagens de erro

    @ConfigField("errors.incorrect-target") private String incorrectTarget;
    @ConfigField("errors.no-permission") private String noPermission;
    @ConfigField("errors.empty-top") private String emptyTop;

    // Mensagens dos comandos

    @ConfigField("command-messages.player-balance") private String playerBalance;
    @ConfigField("command-messages.target-balance") private String targetBalance;
    @ConfigField("command-messages.money-pay") private String moneyPay;
    @ConfigField("command-messages.money-receive") private String moneyReceive;
    @ConfigField("command-messages.money-set") private String moneySet;
    @ConfigField("command-messages.money-add") private String moneyAdd;
    @ConfigField("command-messages.money-help") private List<String> moneyHelp;

    // Money top

    @ConfigField("money-top.header") private String moneyTopHeader;
    @ConfigField("money-top.footer") private String moneyTopFooter;
    @ConfigField("money-top.tycoon-tag") private String tycoonTag;

    public static <T> T get(Function<MessageValue, T> function) { return function.apply(instance); }

}