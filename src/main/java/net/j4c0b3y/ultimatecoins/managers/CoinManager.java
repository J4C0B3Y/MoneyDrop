package net.j4c0b3y.ultimatecoins.managers;

import lombok.experimental.UtilityClass;
import net.j4c0b3y.ultimatecoins.coins.Coin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.j4c0b3y.ultimatecoins.UltimateCoins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class CoinManager {
    private static final UltimateCoins plugin = UltimateCoins.getInstance();

    public List<Coin> getAll() {
        return plugin.getCoinSettings().getSection("coins").getRoutesAsStrings(false).stream().map(Coin::new).toList();
    }

    public List<Coin> sort(List<Coin> coins, SortType type) {
        ArrayList<Coin> result = new ArrayList<>(coins);

        switch (type) {
            case ALPHABETICAL -> result.sort(Comparator.comparing(Coin::getId));
            case VALUE -> result.sort(Comparator.comparingDouble(Coin::getValue).reversed());
        }

        return result;
    }

    public List<Coin> fromAmount(double target) {
        List<Coin> coins = sort(getAll(), SortType.VALUE);
        List<Coin> result = new ArrayList<>();

        for (Coin coin : coins) {
            while (target - coin.getValue() >= 0) {
                target -= coin.getValue();
                result.add(coin);
            }
        }

        return result;
    }

    public List<Coin> fromPercentages(double target, List<String> percentages) {
        List<Coin> result = new ArrayList<>();
        if (percentages.isEmpty()) return result;

        for (String entry : percentages) {
            String[] data = entry.split(" ");
            Coin coin = new Coin(data[1]);

            double percentage = Double.parseDouble(data[0].replace("%", "")) / 100;
            int amount = (int) Math.floor(target * percentage / coin.getValue());

            target -= amount * coin.getValue();

            result.addAll(Collections.nCopies(amount, coin));
        }

        result.addAll(fromAmount(target));

        return result;
    }

    public void spawn(List<Coin> coins, Location location, Player killer, Player victim) {
        coins.forEach(coin -> coin.spawn(location, killer, victim));
    }

    public enum SortType {
        ALPHABETICAL,
        VALUE
    }
}
