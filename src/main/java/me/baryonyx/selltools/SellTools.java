package me.baryonyx.selltools;

import me.baryonyx.selltools.commands.MainCommand;
import me.baryonyx.selltools.configuration.Config;
import me.baryonyx.selltools.hooks.*;
import me.baryonyx.selltools.listeners.SellListener;
import me.baryonyx.selltools.tools.ItemHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class SellTools extends JavaPlugin {
    private Config config;
    private ItemHandler itemHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        VaultHook.hook(this);
        GriefPreventionHook.hook(this);
        ShopGuiPlusHook.hook(this);
        BentoboxHook.hook(this);
        ChestShopHook.hook(this);
        LWCHook.hook(this);
        config = new Config(this);
        itemHandler = new ItemHandler(this, config);
        getServer().getPluginManager().registerEvents(new SellListener(this, config, itemHandler), this);
        getCommand("selltools").setExecutor(new MainCommand(this, config, itemHandler));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
