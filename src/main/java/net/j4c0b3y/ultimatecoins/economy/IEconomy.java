package net.j4c0b3y.ultimatecoins.economy;

import org.bukkit.entity.Player;

public interface IEconomy {

    void addBalance(final Player player, final double amount);

    String getName();

    String getSymbol();

    boolean wholeNumbersEnabled();
    //add more getters
}

