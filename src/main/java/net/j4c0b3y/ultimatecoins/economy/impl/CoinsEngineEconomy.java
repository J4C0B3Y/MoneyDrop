package net.j4c0b3y.ultimatecoins.economy.impl;

import net.j4c0b3y.ultimatecoins.config.MainConfig;
import net.j4c0b3y.ultimatecoins.economy.IEconomy;
import org.bukkit.entity.Player;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public class CoinsEngineEconomy implements IEconomy {

    private final Currency currency;
	public CoinsEngineEconomy(final String currencyName) {
        this.currency = CoinsEngineAPI.getCurrency(currencyName);
	}
	@Override
	public void addBalance(Player player, double amount) {
		if (currency == null) return;
		CoinsEngineAPI.getUserData(player).getCurrencyData(currency).addBalance(amount);
	}

	@Override
	public String getSymbol() {
		return currency.getSymbol();
	}

	@Override
	public String getName() {
		return currency.getName();
	}

	@Override
	public boolean wholeNumbersEnabled() {
		return MainConfig.IntegrationSettings.COINSENGINE_ECO_WHOLE_NUMBERS;
	}

}
