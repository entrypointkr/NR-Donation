package net.teujaem.nrDonation.common.data.chzzk;

import java.util.UUID;

public class StateData {

    private static String state;

    public StateData() {
        createState();
    }

    public String getState() {
        return state;
    }

    private static void createState() {
        state = UUID.randomUUID().toString().replace("-", "");
    }

}
