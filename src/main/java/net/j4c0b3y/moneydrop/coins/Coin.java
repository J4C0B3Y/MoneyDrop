package net.j4c0b3y.moneydrop.coins;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import net.j4c0b3y.moneydrop.MoneyDrop;
import net.j4c0b3y.moneydrop.utils.Metadata;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Coin {
    @Getter private final String id;
    @Getter private final double value;
    @Getter private final Material material;
    @Getter private final int model;

    private final YamlDocument settings;

    public Coin(String id) {
        this.id = id;
        this.settings = MoneyDrop.getInstance().getSettings();
        this.value = settings.getDouble("coins." + id + ".value");
        this.material = Enum.valueOf(Material.class, settings.getString("coins." + id + ".material"));
        this.model = settings.getInt("coins." + id + ".model");
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null && model != 0) {
            meta.setCustomModelData(model);
            item.setItemMeta(meta);
        }

        return item;
    }

    public void spawn(Location location, Player killer, Player victim) {
        Item item = dropItem(location, toItemStack(), settings.getBoolean("drop.naturally"));
        if(item == null) return;

        Metadata.set(item, "coin_id", id);
        Metadata.set(item, "coin_killer", killer != null ? killer.getUniqueId().toString() : null);
        Metadata.set(item, "coin_victim", victim != null ? victim.getUniqueId().toString() : null);
    }

    private Item dropItem(Location location, ItemStack item, boolean naturally) {
        World world = location.getWorld();
        if (world == null) return null;

        return naturally ? location.getWorld().dropItemNaturally(location, item) : location.getWorld().dropItem(location, item);
    }
}
