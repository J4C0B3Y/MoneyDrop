package net.j4c0b3y.ultimatecoins.listeners;

import net.j4c0b3y.ultimatecoins.config.MainConfig;
import net.j4c0b3y.ultimatecoins.utils.Metadata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;

public class MergeListener implements Listener {

    @EventHandler
    public void onMerge(ItemMergeEvent event) {
        if (MainConfig.GeneralSettings.MERGING) return;
        if (!Metadata.has(event.getEntity(), "coin_id")) return;

        event.setCancelled(true);
    }
}
