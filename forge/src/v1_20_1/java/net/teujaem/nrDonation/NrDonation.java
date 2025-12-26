package net.teujaem.nrDonation;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.teujaem.nrDonation.client.NrDonationClient;
import org.slf4j.Logger;

@Mod(NrDonation.MOD_ID)
public class NrDonation {
    public static final String MOD_ID = "nr-donation";
    public static final String NAME = "NR-Donation";

    private static final Logger LOGGER = LogUtils.getLogger();
    private static NrDonationClient client;

    public NrDonation(FMLJavaModLoadingContext ctx) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void init(FMLClientSetupEvent e) {
        client = new NrDonationClient();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
