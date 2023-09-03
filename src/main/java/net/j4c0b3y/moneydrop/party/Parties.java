package net.j4c0b3y.moneydrop.party;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.experimental.UtilityClass;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.party.AbstractParty;
import net.j4c0b3y.moneydrop.MoneyDrop;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class Parties {

    private final MoneyDrop plugin = MoneyDrop.getInstance();
    private final YamlDocument settings = plugin.getSettings();

    public double getMultiplier(Player player) {
        AbstractParty party = getParty(player);

        if (hasPlayers(party)) {
            return Math.pow(settings.getDouble("party.multiplier"), party.getOnlineMembers().size());
        }

        return 1;
    }

    public boolean hasPlayers(AbstractParty party) {
        return party != null && party.getOnlineMembers().size() > 1;
    }

    public int getSize(AbstractParty party) {
        return party.getOnlineMembers().size();
    }

    public List<Player> getPlayers(AbstractParty party) {
        return party.getOnlineMembers().stream().map(PlayerData::getPlayer).toList();
    }

    public boolean isEnabled() {
        return plugin.isMmoHook() && settings.getBoolean("party.enabled");
    }

    public AbstractParty getParty(Player player) {
        return plugin.getMmo().getPlayerData(player).getParty();
    }
}
