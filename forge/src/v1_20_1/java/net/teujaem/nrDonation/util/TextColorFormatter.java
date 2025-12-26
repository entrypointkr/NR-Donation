package net.teujaem.nrDonation.util;

import net.minecraft.network.chat.Component;

public class TextColorFormatter {

    public static Component toColoredText(String message) {
        return Component.literal(message.replace("&", "§"));
    }

    public static String toColoredString(String message) {
        return message.replace("&", "§");
    }

    public static String toUncolored(String message) {
        return message.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
    }

}
