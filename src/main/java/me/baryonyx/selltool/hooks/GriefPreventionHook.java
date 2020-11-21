package me.baryonyx.selltool.hooks;

import me.baryonyx.selltool.SellTool;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GriefPreventionHook {
    public static boolean isHooked = false;

    public static void hook(SellTool plugin) {
        Plugin gp = plugin.getServer().getPluginManager().getPlugin("GriefPrevention");

        if (gp == null) {
            return;
        }

        isHooked = true;
    }

    public static boolean canSell(Player player, Chest chest) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(chest.getLocation(), false, null);
        return claim == null || claim.canSiege(player);
    }
}
