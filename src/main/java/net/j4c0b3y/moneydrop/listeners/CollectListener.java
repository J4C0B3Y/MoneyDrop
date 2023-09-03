package net.j4c0b3y.moneydrop.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.Indyuce.mmocore.party.AbstractParty;
import net.j4c0b3y.moneydrop.MoneyDrop;
import net.j4c0b3y.moneydrop.coins.Coin;
import net.j4c0b3y.moneydrop.hologram.Hologram;
import net.j4c0b3y.moneydrop.party.PickupType;
import net.j4c0b3y.moneydrop.utils.Metadata;
import net.j4c0b3y.moneydrop.party.Parties;
import net.kyori.adventure.audience.Audience;
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
    private final MoneyDrop plugin = MoneyDrop.getInstance();
    private final YamlDocument settings = plugin.getSettings();

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
            PickupType pickup = PickupType.valueOf(settings.getString("party.pickup").toUpperCase());

            if (Parties.hasPlayers(party) && !pickup.equals(PickupType.INDIVIDUAL)) {
                double split = value / Parties.getSize(party);
                Parties.getPlayers(party).forEach(member -> pickup(member, item, pickup.equals(PickupType.SHARED) ? value : split, member.equals(player)));

                return;
            }
        }

        pickup(player, item, value, true);
    }

    private void pickup(Player player, Item item, double value, boolean spawnHologram) {
        item.remove();

        plugin.getEconomy().depositPlayer(player, value);

        String decimal = String.valueOf(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue());
        String whole = String.valueOf((int) value);

        if (settings.getBoolean("pickup.notify.enabled")) {
            String amount = settings.getBoolean("pickup.notify.whole") ? whole : decimal;

            Audience audience = plugin.getAdventure().player(player);
            audience.sendMessage(plugin.getMini().deserialize(settings.getString("pickup.notify.message").replace("<amount>", amount)));
        }

        if (settings.getBoolean("pickup.sound.enabled")) {
            player.playSound(
                player.getLocation(),
                Sound.valueOf(settings.getString("pickup.sound.type")),
                SoundCategory.RECORDS,
                settings.getFloat("pickup.sound.volume"),
                settings.getFloat("pickup.sound.pitch")
            );
        }

        if (spawnHologram && settings.getBoolean("pickup.hologram.enabled")) {
            Location location = item.getLocation().clone()
                .add(0, settings.getDouble("pickup.hologram.y-offset"), 0)
                .add(player.getEyeLocation().getDirection().normalize().multiply(settings.getDouble("pickup.hologram.distance")));

            String amount = settings.getBoolean("pickup.hologram.whole") ? whole : decimal;

            Hologram hologram = new Hologram()
                .teleport(location)
                .setText(settings.getString("pickup.hologram.text").replace("<amount>", amount))
                .spawn();

            int duration = settings.getInt("pickup.hologram.duration");
            double increment = settings.getDouble("pickup.hologram.rise.amount") / duration;
            boolean rise = settings.getBoolean("pickup.hologram.rise.enabled");

            Integer task = rise ? Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> hologram.rise(increment), 1, 0) : null;

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                hologram.despawn();
                if (task != null) Bukkit.getScheduler().cancelTask(task);
            }, duration);
        }

    }

    private boolean canPickUp(Player player, UUID killer, UUID victim) {
        if (killer == null || settings.getBoolean("pickup.others")) return true;

        boolean isKiller = player.getUniqueId().equals(killer);
        boolean isVictim = victim != null && player.getUniqueId().equals(victim);

        return isKiller || (settings.getBoolean("pickup.victim") && isVictim);
    }
}