package net.j4c0b3y.ultimatecoins.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.utils.Metadata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;

public class MergeListener implements Listener {
    private final YamlDocument settings = UltimateCoins.getInstance().getSettings();

    @EventHandler
    public void onMerge(ItemMergeEvent event) {
        if (settings.getBoolean("drop.merge")) return;
        if (!Metadata.has(event.getEntity(), "coin_id")) return;

        event.setCancelled(true);
    }
}
