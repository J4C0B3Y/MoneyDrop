package net.j4c0b3y.ultimatecoins.utils;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;

@UtilityClass
public class MythicUtils {
    public boolean isMythicMob(Entity entity) {
        return getMythicMob(entity) != null;
    }

    public ActiveMob getMythicMob(Entity entity) {
        try (MythicBukkit instance = MythicBukkit.inst()) {
            return instance.getMobManager().getActiveMob(entity.getUniqueId()).orElse(null);
        }
    }
}
