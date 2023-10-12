package net.j4c0b3y.ultimatecoins.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

@UtilityClass
public class PermissionUtils {

    public Double getDouble(Player player, String key) {
        return getDouble(player, key, 0);
    }


    public Double getDouble(Player player, String key, double initial) {
        double result = 0;

        for (String permission : player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).toList()) {
            if (permission.startsWith(key + ".")) {
                double value = Double.parseDouble(permission.replace(key + ".", ""));
                if (value > result) result = value;
            }
        }

        return result == 0 ? initial : result;
    }
}
