package net.j4c0b3y.ultimatecoins.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.j4c0b3y.ultimatecoins.UltimateCoins;

public class MainConfig {
    private static final UltimateCoins plugin = UltimateCoins.getInstance();

    static YamlDocument config = plugin.getConfigFile();

    public static class GeneralSection {
        public static Boolean MONEY_DROP_ON_DEATH_ENABLED = config.getBoolean("playersLoseMoneyOnDeath.enabled");
        public static Double MONEY_DROP_ON_DEATH_PERCENTAGE = config.getDouble("playersLoseMoneyOnDeath.percentage");
    }

    public static class CoinSection {
        //public static String COINSENGINE_CURRENCY = config.getString("coins.coinsengine-currency");

        public static Boolean MMOCORE_PARTY_INTEGRATION_ENABLED = config.getBoolean("coins.mmocore-party-integration.enabled");
        public static Double MMOCORE_PARTY_INTEGRATION_MULTIPLIER = config.getDouble("coins.mmocore-party-integration.multiplier");
        public static String MMOCORE_PARTY_INTEGRATION_DISTRIBUTION_TYPE = config.getString("coins.mmocore-party-integration.pickup");

        public static Boolean NOTIFY_PICKUP = config.getBoolean("coins.notify-player-on-pickup");
        public static Boolean MERGING = config.getBoolean("coins.coin-merging");
        public static Boolean RANDOM_OFFSET = config.getBoolean("coins.random-offset");
        public static Boolean PICKUP_OTHERS = config.getBoolean("coins.can-players-pickup-others");
        public static Boolean PICKUP_OWN = config.getBoolean("coins.can-player-pickup-own");

        public static Boolean PICKUP_SOUND_ENABLED = config.getBoolean("coins.pickup-sound.enabled");
        public static String PICKUP_SOUND_VALUE = config.getString("coins.pickup-sound.sound");
        public static Double PICKUP_SOUND_PITCH = config.getDouble("coins.pickup-sound.pitch");
        public static Double PICKUP_SOUND_VOLUME = config.getDouble("coins.pickup-sound.volume");

        public static Boolean HOLOGRAM_ENABLED = config.getBoolean("coins.hologram-animation.enabled");
        public static String HOLOGRAM_TEXT = config.getString("coins.hologram-animation.hologram-text");
        public static Integer HOLOGRAM_DURATION = config.getInt("coins.hologram-animation.duration");
        public static Double HOLOGRAM_Y_OFFSET = config.getDouble("coins.hologram-animation.y-offset");
        public static Double HOLOGRAM_DISTANCE_FROM_PLAYER = config.getDouble("coins.hologram-animation.distance-from-player");

        public static Boolean HOLOGRAM_RISE_ENABLED = config.getBoolean("coins.hologram-animation.hologram-rise.enabled");
        public static Double HOLOGRAM_RISE_AMOUNT = config.getDouble("coins.hologram-animation.hologram-rise.amount");
    }

    public static class MessageSection {
        public static String NO_PERMISSION_TEXT = config.getString("messages.no-permission");
        public static String USAGE_TEXT = config.getString("messages.usage");
        public static String RELOAD_TEXT = config.getString("messages.reloaded");
        public static String PLAYER_DEATH_MESSAGE_TEXT = config.getString("messages.player-death-message");
        public static String COIN_PICKUP_MESSAGE = config.getString("messages.coin-pickup-message");
    }
}
