package net.j4c0b3y.ultimatecoins.party;

import lombok.experimental.UtilityClass;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.party.AbstractParty;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.config.MainConfig;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class Parties {
    private final UltimateCoins plugin = UltimateCoins.getInstance();

    public double getMultiplier(Player player) {
        AbstractParty party = getParty(player);

        if (hasPlayers(party)) {
            return Math.pow(MainConfig.IntegrationSettings.MMOCORE_PARTY_INTEGRATION_MULTIPLIER, party.getOnlineMembers().size());
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
        return plugin.isMmoHook() && MainConfig.IntegrationSettings.MMOCORE_PARTY_INTEGRATION_ENABLED;
    }

    public AbstractParty getParty(Player player) {
        return plugin.getMmo().getPlayerData(player).getParty();
    }
}

