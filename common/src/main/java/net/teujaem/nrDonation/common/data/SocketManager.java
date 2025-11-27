package net.teujaem.nrDonation.common.data;

import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.handler.donation.chzzk.ChzzkCrateSocket;

public class SocketManager {

    private static String chzzk;

    public void setUrl(PlatformType platformType) throws Exception {

        if (platformType.equals(PlatformType.CHZZK)) {
            ChzzkCrateSocket chzzkCrateSocket = new ChzzkCrateSocket();
            SocketManager.chzzk = chzzkCrateSocket.getUrl(MainAPI.getInstance().getDataClassManager().getAccessToken().getChzzk());
            MainAPI.getInstance().runChzzkClient(SocketManager.chzzk);
        }

    }

    public String getChzzk() {
        return chzzk;
    }

}
