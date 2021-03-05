package me.baryonyx.selltools.hooks;

import me.baryonyx.selltools.SellTools;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;

import java.util.Optional;

public class BentoboxHook {
    public static boolean isHooked = false;

    public static void hook(SellTools plugin) {
        Plugin bp = plugin.getServer().getPluginManager().getPlugin("BentoBox");

        if (bp == null) {
            return;
        }

        isHooked = true;
    }

    public static boolean canSell(Player player, Chest chest) {
        Optional island = BentoBox.getInstance().getIslands().getIslandAt(chest.getLocation());
        if (island.isPresent()) {
            User user = BentoBox.getInstance().getPlayers().getUser(player.getUniqueId());
            return ((Island)island.get()).isAllowed(user, Flags.CONTAINER);
        }

        return false;
    }
}
