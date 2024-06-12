package net.j4c0b3y.ultimatecoins.economy.impl;

import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.config.MainConfig;
import net.j4c0b3y.ultimatecoins.economy.IEconomy;
import org.bukkit.entity.Player;

public class VaultEconomy implements IEconomy {

	@Override
	public void addBalance(Player player, double amount) {
		UltimateCoins.getInstance().getEconomy().depositPlayer(player, amount);
	}

	@Override
	public String getName() {
		return UltimateCoins.getInstance().getEconomy().getName();
	}

	@Override
	public String getSymbol() {
		return "$";
	}

	@Override
	public boolean wholeNumbersEnabled() {
		return MainConfig.IntegrationSettings.VAULT_ECO_WHOLE_NUMBERS;
	}
}
