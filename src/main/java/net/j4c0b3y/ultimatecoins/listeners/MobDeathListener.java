package net.j4c0b3y.ultimatecoins.listeners;

import com.sk89q.worldguard.protection.flags.StateFlag;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.managers.CoinManager;
import net.j4c0b3y.ultimatecoins.party.Parties;
import net.j4c0b3y.ultimatecoins.utils.MythicUtils;
import net.j4c0b3y.ultimatecoins.utils.PermissionUtils;
import net.j4c0b3y.ultimatecoins.utils.WorldGuardUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobDeathListener implements Listener {
    private final UltimateCoins plugin = UltimateCoins.getInstance();
    private final YamlDocument settings = plugin.getCoinSettings();
    private final Random random = new Random();

    @EventHandler
    @SuppressWarnings("SpellCheckingInspection")
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (entity.getType().equals(EntityType.PLAYER)) return;
        if (killer == null || !killer.getType().equals(EntityType.PLAYER)) return;
        if (plugin.isWorldGuardHook() && WorldGuardUtils.checkFlag(killer.getPlayer(), plugin.getMobFlag(), StateFlag.State.DENY)) return;

        Section section;

        if (plugin.isMythicHook() && MythicUtils.isMythicMob(entity)) {
            section = settings.getOptionalSection("death.mob.mythic." + MythicUtils.getMythicMob(entity).getMobType()).orElse(settings.getSection("death.mob.mythic.default"));
        } else {
        section = settings.getOptionalSection("mob-settings.vanilla." + entity.getType().toString().toLowerCase()).orElse(settings.getSection("mob-settings.vanilla.default." + (entity instanceof Monster ? "hostile-mobs" : "passive-mobs")));
        }

        double[] raw = Arrays.stream(section.getString("amount").split("-")).mapToDouble(Double::parseDouble).toArray();
        double amount = raw.length > 1 ? random.nextDouble() * (raw[1] - raw[0]) + raw[0] : raw[0];
        if (Parties.isEnabled()) amount *= Parties.getMultiplier(killer);
        amount *= PermissionUtils.getDouble(killer, "ultimatecoins.mob.multiplier", 1);

        List<String> coins = section.getStringList("coins");
        CoinManager.spawn(!coins.isEmpty() ? CoinManager.fromPercentages(amount, coins) : CoinManager.fromAmount(amount), entity.getLocation(), killer, null);
    }
}
