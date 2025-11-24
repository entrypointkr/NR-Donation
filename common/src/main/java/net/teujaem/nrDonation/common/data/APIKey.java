package net.teujaem.nrDonation.common.data;

import java.util.HashMap;

public class APIKey {

    private static final HashMap<PlatformType, String> ID = new HashMap<>();
    private static final HashMap<PlatformType, String> SECRET = new HashMap<>();

    public void setId(PlatformType platformType, String id)  {
        ID.put(platformType, id);
    }

    public String getId(PlatformType platformType) {
        return ID.get(platformType);
    }


    public void setSecret(PlatformType platformType, String id)  {
        SECRET.put(platformType, id);
    }

    public String getSecret(PlatformType platformType) {
        return SECRET.get(platformType);
    }
}
