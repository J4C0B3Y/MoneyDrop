package net.j4c0b3y.ultimatecoins.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class CommandManager {
    @Getter private final SimpleCommandMap map;

    public CommandManager() {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            map = (SimpleCommandMap) field.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void register(Command command) {
        map.register(command.getName(), command);
    }
}
