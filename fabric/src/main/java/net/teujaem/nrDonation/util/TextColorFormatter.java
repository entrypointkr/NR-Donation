package net.teujaem.nrDonation.util;

import net.minecraft.text.Text;

public class TextColorFormatter {

    public static Text toColoredText(String message) {
        return Text.of(message.replace("&", "§"));
    }

    public static String toColoredString(String message) {
        return message.replace("&", "§");
    }

    public static String toUncolored(String message) {
        return message.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
    }

}
