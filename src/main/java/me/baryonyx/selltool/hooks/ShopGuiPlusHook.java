package me.baryonyx.selltool.hooks;

import me.baryonyx.selltool.SellTool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ShopGuiPlusHook {
    public static boolean isHooked = false;

    public static void hook(SellTool plugin) {
        Plugin shopGui = plugin.getServer().getPluginManager().getPlugin("ShopGuiPlus");

        if (shopGui != null) {
            Bukkit.getLogger().info("Hooking shopgui");
            isHooked = true;
        }
    }
}
