package net.j4c0b3y.ultimatecoins;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import net.Indyuce.mmocore.api.MMOCoreAPI;
//import net.j4c0b3y.ultimatecoins.commands.CommandManager;
//import net.j4c0b3y.ultimatecoins.commands.impl.UltimateCoinsCommand;
import net.j4c0b3y.ultimatecoins.listeners.CollectListener;
import net.j4c0b3y.ultimatecoins.listeners.MergeListener;
import net.j4c0b3y.ultimatecoins.listeners.MobDeathListener;
import net.j4c0b3y.ultimatecoins.listeners.PlayerDeathListener;
import net.j4c0b3y.ultimatecoins.managers.EconomyManager;
import net.j4c0b3y.ultimatecoins.utils.ExceptionUtils;
import net.j4c0b3y.ultimatecoins.utils.SimpleDocument;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public class UltimateCoins extends JavaPlugin {
    @Getter private static UltimateCoins instance;

    //@Getter private BukkitAudiences adventure;
    //@Getter private MiniMessage mini;
    @Getter private Economy economy;
    @Getter private boolean mmoHook = false;
    @Getter private MMOCoreAPI mmo;
    @Getter private boolean worldGuardHook = false;
    @Getter private StateFlag playerFlag;
    @Getter private StateFlag mobFlag;
    @Getter private boolean mythicHook = false;

    @Getter
    private YamlDocument configFile;
    @Getter
    private YamlDocument coinSettings;

    @Getter
    private final EconomyManager economyManager = new EconomyManager(this);

    //private final CommandManager commands = new CommandManager();

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

    private void loadConfigFiles() {
        //When create a config files. when you are loading it directly from resources you can leave file location null
        configFile = SimpleDocument.create("config.yml", null);
        coinSettings = SimpleDocument.create("coins.yml", null);
    }

    @Override
    public void onEnable() {
        instance = this;

        InitializeEconomy();

        loadConfigFiles();

        //adventure = BukkitAudiences.create(this);
        //mini = MiniMessage.builder().tags(TagResolver.builder().resolver(StandardTags.color()).build()).build();

        hookMMOCore();
        hookMythic();

        registerCommands();
        registerListeners();
    }

    public long reload() {
        long start = System.currentTimeMillis();

        try {
            configFile.reload();
            getLogger().info("Reloaded " + configFile.getName() + "!");
            coinSettings.reload();
            getLogger().info("Reloaded " + coinSettings.getName() + "!");
        } catch (IOException exception) {
            getLogger().severe(ExceptionUtils.getStackTrace(exception));
        }

        return System.currentTimeMillis() - start;
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
        //commands.register(new UltimateCoinsCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CollectListener(), this);
        getServer().getPluginManager().registerEvents(new MergeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
    }

    private void InitializeEconomy() {
        if (!hasVaultEconomy()) {
            getLogger().severe(String.format("[%s] - Vault not found! Disabling...", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        economyManager.setupEconomy();
    }

    private boolean hasVaultEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}
