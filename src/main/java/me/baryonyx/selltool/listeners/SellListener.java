package me.baryonyx.selltool.listeners;

import me.baryonyx.selltool.SellTool;
import me.baryonyx.selltool.configuration.Config;
import me.baryonyx.selltool.hooks.GriefPreventionHook;
import me.baryonyx.selltool.hooks.ShopGuiPlusHook;
import me.baryonyx.selltool.hooks.VaultHook;
import me.baryonyx.selltool.tools.ItemHandler;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SellListener implements Listener {
    private SellTool plugin;
    private Config config;
    private ItemHandler itemHandler;

    public SellListener(SellTool plugin, Config config, ItemHandler itemHandler) {
        this.plugin = plugin;
        this.config = config;
        this.itemHandler = itemHandler;
    }

    @EventHandler
    public void chestClickEvent(@NotNull PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST && event.getItem() != null && itemHandler.isSellWand(event.getItem())) {
            Chest chest = (Chest) event.getClickedBlock().getState();
            Inventory inventory = chest.getInventory();
            Player player = event.getPlayer();

            if (inventory.isEmpty()) {
                event.setCancelled(true);
                return;
            }

            if (GriefPreventionHook.isHooked && !GriefPreventionHook.canSell(player, chest)) {
                player.sendMessage("You cannot sell here! This chest is protected.");
                event.setCancelled(true);
                return;
            }

            if (ShopGuiPlusHook.isHooked && sellItems(player, inventory, event.getItem())) {
                decrementUses(event.getItem(), player.getInventory());
            }

            event.setCancelled(true);
        }
    }

    private double getToolModifierPrice(ItemStack item) {
        String type = itemHandler.getItemType(item);
        double modifier = config.getModifierPrice(type);
        return modifier == 0 ? 1 : modifier;
    }

    private boolean sellItems(Player player, Inventory inventory, ItemStack tool) {
        double totalPrice = 0;
        ItemStack[] items = inventory.getContents();
        double toolModifier = getToolModifierPrice(tool);

        for (ItemStack item : items) {
            double itemPrice = ShopGuiPlusApi.getItemStackPriceSell(player, item);

            if (itemPrice > 0) {
                inventory.remove(item);
                totalPrice += itemPrice * toolModifier;
            }
        }

        if (totalPrice == 0) {
            return false;
        }

        totalPrice = Math.round(totalPrice*100d)/100d;

        VaultHook.getEconomy().depositPlayer(player, totalPrice);
        player.sendMessage(config.getSellMessage().replace("%total%", String.valueOf(totalPrice)));
        return true;
    }

    private void decrementUses(ItemStack item, Inventory inventory) {
        int uses = itemHandler.getItemUses(item) - 1;

        if (uses <= 0) {
            inventory.remove(item);
        }
        else {
            itemHandler.setItemUses(item, uses);
        }
    }
}
