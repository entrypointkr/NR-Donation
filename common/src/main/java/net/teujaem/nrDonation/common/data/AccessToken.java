package net.teujaem.nrDonation.common.data;

import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.handler.donation.chzzk.ChzzkCrateAccessToken;
import net.teujaem.nrDonation.common.handler.donation.soop.SoopCrateAccessToken;

public class AccessToken {

    private static String soop;
    private static String chzzk;

    public void setAccessToken(PlatformType platformType, String code) {

        String token = getAccessToken(platformType, code);

        if (platformType.equals(PlatformType.CHZZK)) {
            AccessToken.chzzk = token;
        }

        if (platformType.equals(PlatformType.SOOP)) {
            AccessToken.soop = token;
        }

    }

    public String getChzzk() {
        return chzzk;
    }

    public String getSoop() {
        return soop;
    }

    public void reset(PlatformType platformType) {
        if (platformType.equals(PlatformType.CHZZK)) {
            AccessToken.chzzk = null;
        }

        if (platformType.equals(PlatformType.SOOP)) {
            AccessToken.soop = null;
        }
    }

    private String getAccessToken(PlatformType platformType, String code) {

        APIKey apiKey = MainAPI.getInstance().getDataClassManager().getApiKey();

        if (platformType.equals(PlatformType.SOOP)) {
            SoopCrateAccessToken crateAccessToken = new SoopCrateAccessToken(apiKey.getId(PlatformType.SOOP), apiKey.getSecret(PlatformType.SOOP));
            try {
                return crateAccessToken.getAccessToken(code);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (platformType.equals(PlatformType.CHZZK)) {
            ChzzkCrateAccessToken crateAccessToken = new ChzzkCrateAccessToken(apiKey.getId(PlatformType.CHZZK), apiKey.getSecret(PlatformType.CHZZK));
            try {
                return crateAccessToken.getAccessToken(code, MainAPI.getInstance().getDataClassManager().getStateData().getState());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

}
