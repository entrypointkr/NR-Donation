package net.teujaem.nrDonation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.teujaem.nrDonation.client.NrDonationClient;
import net.teujaem.nrDonation.common.data.PlatformType;

import static net.minecraft.commands.Commands.literal;

public class NrDonationCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> getPlatformAuthCmd(NrDonationClient client, PlatformType platformType) {
        String label;
        switch (platformType) {
            case SOOP -> label = "숲";
            case CHZZK -> label = "치지직";
            default -> throw new UnsupportedOperationException("Unknown platform type: " + platformType.name());
        }
        return literal(label)
                .then(
                        literal("로그인")
                                .executes(source -> {
                                    client.login(platformType);
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .then(
                        literal("로그아웃")
                                .executes(source -> {
                                    client.logout(platformType);
                                    return Command.SINGLE_SUCCESS;
                                })
                );
    }

    public static void register(NrDonationClient client, CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(getPlatformAuthCmd(client, PlatformType.SOOP));
        dispatcher.register(getPlatformAuthCmd(client, PlatformType.CHZZK));
    }
}
