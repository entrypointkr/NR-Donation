package net.teujaem.nrDonation.util;

import org.bukkit.ChatColor;

public class TextColorFormatter {

    public static String toColored(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String toUncolored(String message) {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
    }

}
