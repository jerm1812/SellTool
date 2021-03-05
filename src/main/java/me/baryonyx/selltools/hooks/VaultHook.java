package me.baryonyx.selltools.hooks;

import me.baryonyx.selltools.SellTools;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook {
    private static Economy economy;
    public static boolean isHooked = false;

    // Hooks vault
    public static boolean hook(@NotNull SellTools plugin) {
        RegisteredServiceProvider<Economy> service = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (service == null)
            return false;

        economy = service.getProvider();
        isHooked = true;
        return true;
    }

    // Returns the instance of the Vault economy
    public static Economy getEconomy() {
        return economy;
    }
}
