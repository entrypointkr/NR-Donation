package net.teujaem.nrDonation.common.data;

public class LoginPlatform {

    private static PlatformType platformType = null;

    public void setPlatformType(PlatformType platformType) {
        LoginPlatform.platformType = platformType;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }
}
