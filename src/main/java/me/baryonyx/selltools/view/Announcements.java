package me.baryonyx.selltools.view;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class Announcements {

    @NotNull
    public static String colloredMessage(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
