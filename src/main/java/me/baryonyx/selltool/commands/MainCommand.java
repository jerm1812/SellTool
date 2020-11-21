package me.baryonyx.selltool.commands;

import me.baryonyx.selltool.SellTool;
import me.baryonyx.selltool.configuration.Config;
import me.baryonyx.selltool.tools.ItemHandler;
import me.baryonyx.selltool.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements TabExecutor {
    private SellTool plugin;
    private Config config;
    private ItemHandler itemHandler;

    public MainCommand(SellTool plugin, Config config, ItemHandler itemHandler) {
        this.plugin = plugin;
        this.config = config;
        this.itemHandler = itemHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return helpCommand(sender);
        }
        if (args[0].equalsIgnoreCase("give")) {
            giveCommand(args, sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            reloadCommand(sender);
            return true;
        }

        return false;
    }

    private boolean helpCommand(CommandSender player) {
        return false;
    }

    private void giveCommand(String[] args, CommandSender sender) {
        if (args.length < 3 || args.length > 4) {
            sender.sendMessage("Incorrect usage. Ex: /st give {user} {type} {uses}");
            return;
        }

        if (!sender.hasPermission(Permissions.give)) {
            sender.sendMessage(config.getNoPermMessage().replace("%perm%", Permissions.give).replace("%cmd", "/st give"));
            return;
        }

        ItemStack item = itemHandler.createSellTool(args[2]);

        if (item == null) {
            sender.sendMessage("There was an error creating the sell tool " + args[2]);
            return;
        }

        int uses;

        if (args.length == 4) {
            uses = 0;

            try {
                uses = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Please provide a number for uses!");
                return;
            }

            if (uses <= 0) {
                sender.sendMessage("A sell tool's uses needs to be above 0!");
                return;
            }
        }
        else {
            uses = config.getDefaultUses();
        }

        itemHandler.setItemType(item, args[2]);
        itemHandler.setItemUses(item, uses);

        givePlayerWand(item, sender, args[1]);
    }

    private void givePlayerWand(ItemStack item, CommandSender sender, String name) {
        Player player = plugin.getServer().getPlayer(name);

        if (player == null) {
            sender.sendMessage("Tried to give a sell tool to " + name + " but it appears that they are offline");
            return;
        }

        player.getInventory().addItem(item);
    }

    private void reloadCommand(CommandSender sender) {
        if (sender.hasPermission(Permissions.reload)) {
            config.reload();
            sender.sendMessage("SellTool has reloaded!");
        }
        else {
            sender.sendMessage(config.getNoPermMessage().replace("%perm%", Permissions.reload).replace("%cmd%", "/st reload"));
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("give", "reload");
        }
        else if (args.length == 3) {
            return config.getTools().stream().collect(Collectors.toList());
        }

        return null;
    }
}
