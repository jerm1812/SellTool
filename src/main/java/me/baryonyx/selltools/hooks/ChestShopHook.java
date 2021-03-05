package me.baryonyx.selltools.hooks;

import com.Acrobot.ChestShop.Plugins.ChestShop;
import me.baryonyx.selltools.SellTools;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChestShopHook {
    public static boolean isHooked = false;

    public static void hook(SellTools plugin) {
        Plugin chestShop = plugin.getServer().getPluginManager().getPlugin("ChestShop");

        if (chestShop == null) {
            return;
        }

        isHooked = true;
    }

    public static boolean canSell(Player player, Block block) {
        return ChestShop.canAccess(player, block);
    }
}
