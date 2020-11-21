package me.baryonyx.selltool.configuration;

import me.baryonyx.selltool.SellTool;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

public class Config {
    private SellTool plugin;
    private FileConfiguration config;

    public Config(SellTool plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reload() {
        config = null;
        loadConfig();
    }

    private void loadConfig() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public ConfigurationSection getItem() {
        return config.getConfigurationSection("item");
    }

    public String getItemId() {
        return config.getString("item.id");
    }

    public Set<String> getTools() {
        return config.getConfigurationSection("wands").getKeys(false);
    }

    @Nullable
    public ConfigurationSection getSellTool(String name) {
        return config.getConfigurationSection("wands." + name);
    }

    public Integer getDefaultUses() {
        return config.getInt("default-uses");
    }

    public Double getModifierPrice(String type) {
        return config.getDouble("wands." + type + ".bonus");
    }

    public String getDisplayName(String type) {
        return config.getString("wands." + type + ".display-name");
    }

    public String getSellMessage() {
        return config.getString("sell-message");
    }

    public String getNoPermMessage() {
        return config.getString("no-perm-message");
    }
}
