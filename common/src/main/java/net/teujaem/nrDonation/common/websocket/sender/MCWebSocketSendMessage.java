package net.teujaem.nrDonation.common.websocket.sender;

import net.teujaem.nrDonation.common.MainAPI;

public class MCWebSocketSendMessage {

    public void to(String message) {
        MainAPI.getInstance().getDataClassManager().getMcWebSocketClient().send(
                "user//" + message
        );
    }
}
