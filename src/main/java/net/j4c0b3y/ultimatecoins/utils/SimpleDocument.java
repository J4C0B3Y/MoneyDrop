package net.j4c0b3y.ultimatecoins.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class SimpleDocument {

    private static final UltimateCoins plugin = UltimateCoins.getInstance();

    public static YamlDocument create(String fileName, @Nullable String fileLocation) {

        InputStream inputStream;
        YamlDocument configFile = null;

        try {
            if (fileLocation != null) {
                inputStream = Objects.requireNonNull(plugin.getResource(fileLocation + "/" + fileName), "Couldn't load " + fileName);
            } else {
                inputStream = Objects.requireNonNull(plugin.getResource(fileName), "Couldn't load " + fileName);
            }

            configFile = YamlDocument.create(
                    new File(plugin.getDataFolder(), fileName),
                    inputStream,
                    GeneralSettings.builder().setUseDefaults(false).build(),
                    LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.DEFAULT
            );
        } catch (IOException | NullPointerException exception) {
            plugin.getLogger().severe(exception.getMessage());
        }
        return configFile;
    }

}
