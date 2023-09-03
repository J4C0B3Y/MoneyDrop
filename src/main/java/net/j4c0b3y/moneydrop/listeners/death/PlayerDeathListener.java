package net.j4c0b3y.moneydrop.listeners.death;

import com.sk89q.worldguard.protection.flags.StateFlag;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.j4c0b3y.moneydrop.MoneyDrop;
import net.j4c0b3y.moneydrop.coins.Coins;
import net.j4c0b3y.moneydrop.party.Parties;
import net.j4c0b3y.moneydrop.utils.WorldGuardUtils;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerDeathListener implements Listener {
    private final MoneyDrop plugin = MoneyDrop.getInstance();
    private final YamlDocument settings = plugin.getSettings();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (plugin.isWorldGuardHook() && WorldGuardUtils.checkFlag(player, plugin.getPlayerFlag(), StateFlag.State.DENY)) return;

        double amount = plugin.getEconomy().getBalance(player) * settings.getDouble("death.player.percentage") / 100;

        if (Parties.isEnabled()) amount *= Parties.getMultiplier(player);

        Coins.spawn(Coins.fromAmount(amount), player.getLocation(), killer, player);
        plugin.getEconomy().withdrawPlayer(player, amount);

        if (settings.getBoolean("death.player.notify.enabled")) notify(player, amount);
    }

    private void notify(Player player, double value) {
        String decimal = String.valueOf(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue());
        String whole = String.valueOf((int) value);
        String amount = settings.getBoolean("death.player.notify.whole") ? whole : decimal;

        Audience audience = plugin.getAdventure().player(player);
        audience.sendMessage(plugin.getMini().deserialize(settings.getString("death.player.notify.message").replace("<amount>", amount)));
    }
}
