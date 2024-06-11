package net.j4c0b3y.ultimatecoins.managers;


import lombok.Getter;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.economy.impl.CoinsEngineEconomy;
import net.j4c0b3y.ultimatecoins.economy.impl.VaultEconomy;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;

public class EconomyManager {

	@Getter
	private VaultEconomy vaultEconomy;

	@Getter
	private CoinsEngineEconomy coinsEngineEconomy;

    public EconomyManager(UltimateCoins plugin) {
    }

	//add null check

	public void setupEconomy() {
		this.vaultEconomy = new VaultEconomy();

		String currencyName = "coins";
		this.coinsEngineEconomy = new CoinsEngineEconomy(currencyName);


	}
}
