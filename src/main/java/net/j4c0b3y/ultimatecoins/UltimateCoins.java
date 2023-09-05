package net.j4c0b3y.ultimatecoins;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import net.Indyuce.mmocore.api.MMOCoreAPI;
import net.j4c0b3y.ultimatecoins.commands.CommandManager;
import net.j4c0b3y.ultimatecoins.commands.impl.UltimateCoinsCommand;
import net.j4c0b3y.ultimatecoins.listeners.CollectListener;
import net.j4c0b3y.ultimatecoins.listeners.MergeListener;
import net.j4c0b3y.ultimatecoins.listeners.death.MobDeathListener;
import net.j4c0b3y.ultimatecoins.listeners.death.PlayerDeathListener;
import net.j4c0b3y.ultimatecoins.utils.ExceptionUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UltimateCoins extends JavaPlugin {
    @Getter private static UltimateCoins instance;
    @Getter private YamlDocument settings;
    @Getter private BukkitAudiences adventure;
    @Getter private MiniMessage mini;
    @Getter private Economy economy;
    @Getter private boolean mmoHook = false;
    @Getter private MMOCoreAPI mmo;
    @Getter private boolean worldGuardHook = false;
    @Getter private StateFlag playerFlag;
    @Getter private StateFlag mobFlag;
    @Getter private boolean mythicHook = false;

    private final CommandManager commands = new CommandManager();

    @Override @SuppressWarnings("SpellCheckingInspection")
    public void onLoad() {
        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            playerFlag = new StateFlag("ultimatecoins-players", true);
            mobFlag = new StateFlag("ultimatecoins-mobs", true);

            registry.registerAll(List.of(playerFlag, mobFlag));

            worldGuardHook = true;
            getLogger().info("Hooked into WorldGuard!");
        } catch (NoClassDefFoundError error) {
            getLogger().warning("WorldGuard not found!");
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();

        adventure = BukkitAudiences.create(this);
        mini = MiniMessage.builder().tags(TagResolver.builder().resolver(StandardTags.color()).build()).build();

        hookVault();
        hookMMOCore();
        hookMythic();

        registerCommands();
        registerListeners();
    }

    public long reload() {
        long start = System.currentTimeMillis();

        try {
            settings.reload();
            getLogger().info("Reloaded config.yml!");
        } catch (IOException exception) {
            getLogger().severe(ExceptionUtils.getStackTrace(exception));
        }

        return System.currentTimeMillis() - start;
    }

    private void loadConfig() {
        try {
            settings = YamlDocument.create(
                new File(getDataFolder(), "config.yml"),
                Objects.requireNonNull(getResource("config.yml"), "Couldn't load config.yml")
            );
        } catch (IOException | NullPointerException exception) {
            getLogger().severe(exception.getMessage());
        }
    }

    private void hookVault() {
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);

        if (provider == null) {
            getLogger().severe("Vault not found! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        economy = provider.getProvider();

        getLogger().info("Hooked into Vault!");
    }

    private void hookMMOCore() {
        try {
            mmo = new MMOCoreAPI(this);
            mmoHook = true;
            getLogger().info("Hooked into MMOCore!");
        } catch (NoClassDefFoundError error) {
            getLogger().warning("MMOCore not found!");
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void hookMythic() {
        try {
            Class.forName("io.lumine.xikage.mythicmobs.MythicMobs");
            mythicHook = true;
        } catch (ClassNotFoundException exception) {
            getLogger().warning("MythicMobs not found!");
        }
    }

    private void registerCommands() {
        commands.register(new UltimateCoinsCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CollectListener(), this);
        getServer().getPluginManager().registerEvents(new MergeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
    }
}
