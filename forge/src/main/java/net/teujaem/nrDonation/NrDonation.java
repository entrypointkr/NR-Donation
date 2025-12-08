package net.teujaem.nrDonation;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.teujaem.nrDonation.client.NrDonationClient;
import org.apache.logging.log4j.Logger;

// NOTE: https://docs.minecraftforge.net/en/1.12.x/
@Mod(modid = NrDonation.MODID, name = NrDonation.NAME, version = NrDonation.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*")
public class NrDonation {
    public static final String MODID = "nr-donation";
    public static final String NAME = "NR-Donation";
    public static final String VERSION = "1.3.0";

    private static Logger logger;
    private static NrDonationClient client;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            client = new NrDonationClient();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static NrDonationClient getClient() {
        return client;
    }
}
