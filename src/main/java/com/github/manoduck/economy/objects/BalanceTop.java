package com.github.manoduck.economy.objects;

import lombok.Builder;
import lombok.Getter;

@Builder
public class BalanceTop {

    @Getter private String name;
    @Getter private Double balance;

}