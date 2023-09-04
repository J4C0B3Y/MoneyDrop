package net.j4c0b3y.ultimatecoins.hologram;

import lombok.Getter;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

public class Hologram {
    @Getter private ArmorStand entity;
    @Getter private Location location;
    @Getter private String text;

    private final MiniMessage mini = UltimateCoins.getInstance().getMini();

    public Hologram spawn() {
        World world = location.getWorld();
        if (world == null) return this;

        this.entity = world.spawn(location, ArmorStand.class);

        entity.setSmall(true);
        entity.setMarker(true);
        entity.setArms(false);
        entity.setBasePlate(false);
        entity.setGravity(false);
        entity.setVisible(false);
        entity.setCustomName(text);
        entity.setCustomNameVisible(true);
        entity.setAI(false);
        entity.setCollidable(false);
        entity.setInvulnerable(true);

        return this;
    }

    public Hologram teleport(Location location) {
        this.location = location;
        if (entity == null) return this;

        entity.teleport(location);
        return this;
    }

    public void rise(double amount) {
        teleport(location.add(0, amount, 0));
    }

    public Hologram setText(String text) {
        this.text = LegacyComponentSerializer.legacySection().serialize(mini.deserialize(text));
        if (entity == null) return this;

        entity.setCustomName(text);

        return this;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void despawn() {
        entity.remove();
        this.entity = null;
    }
}
