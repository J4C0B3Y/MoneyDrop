package net.j4c0b3y.ultimatecoins.economy;

import org.bukkit.entity.Player;

public interface IEconomy {

    void addBalance(final Player player, final double amount);

    String getIdentifier();

    String getSymbol();

    String getName();

    //add more getters
}

