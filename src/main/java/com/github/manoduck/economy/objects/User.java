package com.github.manoduck.economy.objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Builder
@EqualsAndHashCode
public class User {

    @Getter @Setter private String name;
    @Getter private double balance;

    public Player getPlayer() { return Bukkit.getPlayer(name); }

    public void setBalance(double balance) { this.balance = balance; }

    public void depositBalance(double balance) { this.balance += (int) balance; }

    public void withdrawBalance(double balance) { this.balance -= (int) balance; }

    public boolean hasBalance(double balance) { return this.balance >= balance; }

}