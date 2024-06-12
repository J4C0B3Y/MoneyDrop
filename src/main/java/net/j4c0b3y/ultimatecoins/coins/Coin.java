package net.j4c0b3y.ultimatecoins.coins;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import net.j4c0b3y.ultimatecoins.UltimateCoins;
import net.j4c0b3y.ultimatecoins.config.MainConfig;
import net.j4c0b3y.ultimatecoins.economy.IEconomy;
import net.j4c0b3y.ultimatecoins.utils.Metadata;
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


    @Getter private final IEconomy coinEconomy;

    public Coin(String id) {

        UltimateCoins plugin = UltimateCoins.getInstance();

        YamlDocument settings = plugin.getCoinSettings();

        this.id = id;
        this.value = settings.getDouble("coins." + id + ".value");
        this.material = Enum.valueOf(Material.class, settings.getString("coins." + id + ".material"));
        this.model = settings.getInt("coins." + id + ".model");

        String economyType = settings.getString("coins." + id + ".economy").toUpperCase();

        //This is kept as switch in order to add more economy types
        switch (economyType) {
            case "COINSENGINE":
                this.coinEconomy = plugin.getEconomyManager().getCoinsEngineEconomy();
                break;
            default: //default to vault economy if economyType is invaild
                this.coinEconomy = plugin.getEconomyManager().getVaultEconomy();
                break;
        }
    }

    //uses coin material and model to create a new itemstack
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null && model != 0) {
            meta.setCustomModelData(model);
            item.setItemMeta(meta);
        }

        return item;
    }

    //method to spawn coin
    public void spawn(Location location, Player killer, Player victim) {

        //placeholder for actual config info
        boolean dropNaturally = MainConfig.GeneralSettings.RANDOM_OFFSET;

        Item item = dropItem(location, toItemStack(), dropNaturally);
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
