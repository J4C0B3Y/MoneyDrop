package net.j4c0b3y.ultimatecoins.listeners;


import net.Indyuce.mmocore.party.AbstractParty;
import net.j4c0b3y.ultimatecoins.coins.Hologram;
import net.j4c0b3y.ultimatecoins.party.Parties;
import net.j4c0b3y.ultimatecoins.party.CoinDistributionType;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.coins.Coin;
import net.j4c0b3y.ultimatecoins.economy.IEconomy;
import net.j4c0b3y.ultimatecoins.config.MainConfig;
import net.j4c0b3y.ultimatecoins.utils.Metadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class CollectListener implements Listener {
    private final UltimateCoins plugin = UltimateCoins.getInstance();
    //private final YamlDocument settings = plugin.getCoinsConfig();

    @EventHandler
    public void onCollect(EntityPickupItemEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) return;

        Player player = (Player) event.getEntity();
        Item item = event.getItem();

        String type = Metadata.get(item, "coin_id").map(Object::toString).orElse(null);
        if (type == null) return;

        event.setCancelled(true);

        UUID killer = Metadata.get(item, "coin_killer").map(Object::toString).map(UUID::fromString).orElse(null);
        UUID victim = Metadata.get(item, "coin_victim").map(Object::toString).map(UUID::fromString).orElse(null);

        if (!canPickUp(player, killer, victim)) return;

        Coin coin = new Coin(type);
        double value = coin.getValue() * item.getItemStack().getAmount();

        if (Parties.isEnabled()) {
            AbstractParty party = Parties.getParty(player);
            CoinDistributionType pickup = CoinDistributionType.valueOf(MainConfig.CoinSection.MMOCORE_PARTY_INTEGRATION_DISTRIBUTION_TYPE);

            if (Parties.hasPlayers(party) && !pickup.equals(CoinDistributionType.INDIVIDUAL)) {
                double split = value / Parties.getSize(party);
                Parties.getPlayers(party).forEach(member -> pickup(member, item, pickup.equals(CoinDistributionType.SHARED) ? value : split, member.equals(player), coin.getCoinEconomy()));

                return;
            }
        }

        pickup(player, item, value, MainConfig.CoinSection.HOLOGRAM_ENABLED, coin.getCoinEconomy());
    }

    private void pickup(Player player, Item item, double value, boolean spawnHologram, IEconomy economy) {
        item.remove();

        economy.addBalance(player, value);

        String decimal = String.valueOf(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue());
        String whole = String.valueOf((int) value);

        if (MainConfig.CoinSection.NOTIFY_PICKUP) {
            //String amount = settings.getBoolean("pickup.notify.whole") ? whole : decimal;

            //DO NOT INLINE VARIABLE. IT IS A PLACEHOLDER FOR WHAT IS ABOVE
            String amount = whole;

            //Audience audience = plugin.getAdventure().player(player);
            /*audience.sendMessage(plugin.getMini().deserialize(MainConfig.MessageSection.COIN_PICKUP_MESSAGE)
                    .replace("%amount%", amount)
                    .replace("%currency_symbol%", economy.getSymbol())
                    .replace("%currency_name%", economy.getName()));*/
        }

        if (MainConfig.CoinSection.PICKUP_SOUND_ENABLED) {
            player.playSound(
                player.getLocation(),
                Sound.valueOf(MainConfig.CoinSection.PICKUP_SOUND_VALUE),
                SoundCategory.RECORDS,
                MainConfig.CoinSection.PICKUP_SOUND_VOLUME.floatValue(),
                MainConfig.CoinSection.PICKUP_SOUND_PITCH.floatValue()
            );
        }

        if (spawnHologram && MainConfig.CoinSection.HOLOGRAM_ENABLED) {
            Location location = item.getLocation().clone()
                .add(0, MainConfig.CoinSection.HOLOGRAM_Y_OFFSET, 0)
                .add(player.getEyeLocation().getDirection().normalize().multiply(MainConfig.CoinSection.HOLOGRAM_DISTANCE_FROM_PLAYER));

            //String amount = settings.getBoolean("pickup.hologram.whole") ? whole : decimal;

            //DO NOT INLINE VARIABLE. IT IS A PLACEHOLDER FOR WHAT IS ABOVE
            String amount = whole;

            Hologram hologram = new Hologram()
                .teleport(location)
                .setText(MainConfig.CoinSection.HOLOGRAM_TEXT
                        .replace("%amount%", amount)
                        .replace("%currency_symbol%", economy.getSymbol())
                        .replace("%currency_name%", economy.getName()))
                        //Spawns in hologram
                        .spawn();


            boolean rise = MainConfig.CoinSection.HOLOGRAM_RISE_ENABLED;
            int duration = MainConfig.CoinSection.HOLOGRAM_DURATION;
            double increment = MainConfig.CoinSection.HOLOGRAM_RISE_AMOUNT / duration;



            Integer task = rise ? Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> hologram.rise(increment), 1, 0) : null;

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                hologram.despawn();
                if (task != null) Bukkit.getScheduler().cancelTask(task);
            }, duration);
        }

    }

    private boolean canPickUp(Player player, UUID killer, UUID victim) {
        if (killer == null || MainConfig.CoinSection.PICKUP_OTHERS) return true;

        boolean isKiller = player.getUniqueId().equals(killer);
        boolean isVictim = victim != null && player.getUniqueId().equals(victim);

        return isKiller || MainConfig.CoinSection.PICKUP_OWN && isVictim;
    }
}