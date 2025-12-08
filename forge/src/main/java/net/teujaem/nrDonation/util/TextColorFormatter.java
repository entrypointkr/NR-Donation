package net.teujaem.nrDonation.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TextColorFormatter {

    public static ITextComponent toColoredText(String message) {
        return new TextComponentString(message.replace("&", "§"));
    }

    public static String toColoredString(String message) {
        return message.replace("&", "§");
    }

    public static String toUncolored(String message) {
        return message.replaceAll("§[0-9A-FK-ORa-fk-or]", "");
    }

}
