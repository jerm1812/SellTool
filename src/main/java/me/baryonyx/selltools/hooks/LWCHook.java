package me.baryonyx.selltools.hooks;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import me.baryonyx.selltools.SellTools;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LWCHook {
    public static boolean isHooked = false;

    public static void hook(SellTools plugin) {
        Plugin lwc = plugin.getServer().getPluginManager().getPlugin("LWC");

        if (lwc == null) {
            return;
        }

        Bukkit.getLogger().info("Hooking LWCX");

        isHooked = true;
    }

    public static boolean canSell(Player player, Block block) {
        Protection protection = LWC.getInstance().findProtection(block);

        if (protection == null) {
            return true;
        }

        boolean canAccess = LWC.getInstance().canAccessProtection(player, block);
        Bukkit.getLogger().info("Can access: " + canAccess);
        return canAccess;
    }
}
