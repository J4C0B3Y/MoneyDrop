package net.j4c0b3y.ultimatecoins.listeners;

import com.sk89q.worldguard.protection.flags.StateFlag;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.j4c0b3y.ultimatecoins.config.MainConfig;
import net.j4c0b3y.ultimatecoins.party.Parties;
import net.j4c0b3y.ultimatecoins.utils.PermissionUtils;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.utils.WorldGuardUtils;
import net.j4c0b3y.ultimatecoins.managers.CoinManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerDeathListener implements Listener {
    private final UltimateCoins plugin = UltimateCoins.getInstance();

    @EventHandler
    @SuppressWarnings("SpellCheckingInspection")
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (MainConfig.GeneralSettings.MONEY_DROP_ON_DEATH_ENABLED) return;
        if (plugin.isWorldGuardHook() && WorldGuardUtils.checkFlag(player, plugin.getPlayerFlag(), StateFlag.State.DENY)) return;

        double percentage = MainConfig.GeneralSettings.MONEY_DROP_ON_DEATH_PERCENTAGE;
        percentage = PermissionUtils.getDouble(player, "ultimatecoins.percentage", percentage);

        double amount = plugin.getEconomy().getBalance(player) * percentage / 100;

        if (Parties.isEnabled()) amount *= Parties.getMultiplier(player);

        CoinManager.spawn(CoinManager.fromAmount(amount), player.getLocation(), killer, player);

        //add logic to detemine which economy
        plugin.getEconomy().withdrawPlayer(player, amount);


        //notify method needs to be reimplemented

        /*if (settings.getBoolean("death.player.notify.enabled")) {
            economy.wholeNumbersEnabled();
        }*/
    }

    /*private void notify(Player player, double value) {
        String decimal = String.valueOf(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue());
        String whole = String.valueOf((int) value);
        String amount = settings.getBoolean("death.player.notify.whole") ? whole : decimal;

        Audience targetPlayer = plugin.getAdventure().player(player);
        targetPlayer.sendMessage(plugin.getMini().deserialize(MainConfig.MessageSection.PLAYER_DEATH_MESSAGE_TEXT).replace("<amount>", amount));
    }*/
}