package me.baryonyx.selltools.hooks;

import me.baryonyx.selltools.SellTools;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ShopGuiPlusHook {
    public static boolean isHooked = false;

    public static void hook(SellTools plugin) {
        Plugin shopGui = plugin.getServer().getPluginManager().getPlugin("ShopGuiPlus");

        if (shopGui != null) {
            Bukkit.getLogger().info("Hooking shopgui");
            isHooked = true;
        }
    }
}
