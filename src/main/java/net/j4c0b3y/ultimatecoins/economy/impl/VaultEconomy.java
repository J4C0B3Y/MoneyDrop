package net.j4c0b3y.ultimatecoins.economy.impl;

import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.economy.IEconomy;
import org.bukkit.entity.Player;

public class VaultEconomy implements IEconomy {

	@Override
	public void addBalance(Player player, double amount) {
		UltimateCoins.getInstance().getEconomy().depositPlayer(player, amount);
	}

	@Override
	public String getIdentifier() {
		return "vault";
	}

	@Override
	public String getSymbol() {
		return "$";
	}

	@Override
	public String getName() {
		return UltimateCoins.getInstance().getEconomy().getName();
	}
}
