package me.baryonyx.selltool;

import me.baryonyx.selltool.commands.MainCommand;
import me.baryonyx.selltool.configuration.Config;
import me.baryonyx.selltool.hooks.GriefPreventionHook;
import me.baryonyx.selltool.hooks.ShopGuiPlusHook;
import me.baryonyx.selltool.hooks.VaultHook;
import me.baryonyx.selltool.listeners.SellListener;
import me.baryonyx.selltool.tools.ItemHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class SellTool extends JavaPlugin {
    private Config config;
    private ItemHandler itemHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        VaultHook.hook(this);
        GriefPreventionHook.hook(this);
        ShopGuiPlusHook.hook(this);
        config = new Config(this);
        itemHandler = new ItemHandler(this, config);
        getServer().getPluginManager().registerEvents(new SellListener(this, config, itemHandler), this);
        getCommand("selltool").setExecutor(new MainCommand(this, config, itemHandler));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
    }
}
