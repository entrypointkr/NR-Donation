package net.teujaem.nrDonation.common.handler;

import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.data.PlatformType;
import net.teujaem.nrDonation.common.websocket.sender.MCWebSocketSendMessage;

public class WebSocketEventHandler {

    private static String playerName;

    public WebSocketEventHandler(String playerName) {
        WebSocketEventHandler.playerName = playerName;
    }

    public void action(String[] messages) {

        if (messages[0].equals("getSessionsId")) {
            MCWebSocketSendMessage mcWebSocketSendMessage = new MCWebSocketSendMessage();
            mcWebSocketSendMessage.to("setSessionsId//" + playerName);
        }

        if (messages[0].equals("put.apiKey")) {
            if (messages[1].equals("soop")) {
                MainAPI.getInstance().getDataClassManager().getApiKey().setId(PlatformType.SOOP, messages[2]);
                MainAPI.getInstance().getDataClassManager().getApiKey().setSecret(PlatformType.SOOP, messages[3]);
            }
            if (messages[1].equals("chzzk")) {
                MainAPI.getInstance().getDataClassManager().getApiKey().setId(PlatformType.CHZZK, messages[2]);
                MainAPI.getInstance().getDataClassManager().getApiKey().setSecret(PlatformType.CHZZK, messages[3]);
            }
        }

        if (messages[0].equals("put.apiServer")) {
            if (messages[1].equals("soop")) {
                MainAPI.getInstance().getDataClassManager().getNodeJSUrl().setURL(messages[2]);
            }
        }

    }

}
