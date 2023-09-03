package net.j4c0b3y.moneydrop.commands.impl;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.j4c0b3y.moneydrop.MoneyDrop;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class MoneyDropCommand extends Command {
    private final MoneyDrop plugin;
    private final MiniMessage mini;
    private final YamlDocument settings;

    public MoneyDropCommand() {
        super("moneydrop");

        this.plugin = MoneyDrop.getInstance();
        this.mini = plugin.getMini();
        this.settings = plugin.getSettings();

        setAliases(List.of("md"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        Audience audience = plugin.getAdventure().sender(sender);

        if (!sender.hasPermission("moneydrop.admin")) {
            audience.sendMessage(mini.deserialize(settings.getString("messages.no-permission")));
            return true;
        }

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            audience.sendMessage(mini.deserialize(settings.getString("messages.usage").replace("<alias>", alias)));
            return true;
        }

        long time = plugin.reload();
        audience.sendMessage(mini.deserialize(settings.getString("messages.reloaded").replace("<time>", String.valueOf(time))));
        return true;
    }

    @Override @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1 && sender.hasPermission("moneydrop.admin")) {
            completions.add("reload");
        }

        return completions;
    }
}
