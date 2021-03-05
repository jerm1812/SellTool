package me.baryonyx.selltools.tools;

import me.baryonyx.selltools.SellTools;
import me.baryonyx.selltools.configuration.Config;
import me.baryonyx.selltools.view.Announcements;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemHandler {
    private Config config;
    private NamespacedKey usesKey;
    private NamespacedKey typeKey;

    public ItemHandler(SellTools plugin, Config config) {
        this.config = config;

        usesKey = new NamespacedKey(plugin, "uses");
        typeKey = new NamespacedKey(plugin, "type");
    }

    public ItemStack createSellTool(String name) {
        ConfigurationSection tool = config.getSellTool(name);

        if (tool != null) {
            ItemStack item = new ItemStack(Material.matchMaterial(config.getItemId()));
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(tool.getInt("custom-model-data"));
            item.setItemMeta(meta);
            return item;
        }

        return null;
    }

    private void updateLore(ItemStack item, int uses, String name) {
        ConfigurationSection wand = config.getItem();
        List<String> lore = wand.getStringList("lore").stream().map(Announcements::colloredMessage).collect(Collectors.toList());
        List<String> newLore = new ArrayList<>();

        for (String string : lore) {
            newLore.add(string.replace("%uses%", String.valueOf(uses)).replace("%bonus%", String.valueOf(config.getSellTool(name).getDouble("bonus"))));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setLore(newLore);
        item.setItemMeta(meta);
    }

    public Boolean isSellWand(ItemStack item) {
        try {
            ItemMeta meta = item.getItemMeta();
            return Objects.requireNonNull(meta).getPersistentDataContainer().has(typeKey, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not get sell tool type because item meta was null");
        }

        return false;
    }


    public void setItemType(ItemStack item, String type) {
        try {
            ItemMeta meta = item.getItemMeta();
            Objects.requireNonNull(meta).getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, type);
            meta.setDisplayName(Announcements.colloredMessage(config.getDisplayName(type)));
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not set sell tool type because item meta was null");
        }
    }

    public String getItemType(ItemStack item) {
        try {
            ItemMeta meta = item.getItemMeta();
            return Objects.requireNonNull(meta).getPersistentDataContainer().get(typeKey, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not get sell tool type because item meta was null");
        }

        return null;
    }

    public void setItemUses(ItemStack item, int uses) {
        try {
            updateLore(item, uses, getItemType(item));
            ItemMeta meta = item.getItemMeta();
            Objects.requireNonNull(meta).getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, uses);
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not set sell tool uses because item meta was null");
        }
    }

    public int getItemUses(ItemStack item) {
        try {
            ItemMeta meta = item.getItemMeta();
            return Objects.requireNonNull(meta).getPersistentDataContainer().get(usesKey, PersistentDataType.INTEGER);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not get sell tool uses because item meta was null");
        }

        return 0;
    }
}
