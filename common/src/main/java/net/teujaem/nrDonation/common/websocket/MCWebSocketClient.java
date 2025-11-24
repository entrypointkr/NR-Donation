package net.teujaem.nrDonation.common.websocket;

import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.handler.WebSocketEventHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MCWebSocketClient extends WebSocketClient {

    private static String playerName;

    public MCWebSocketClient(String playerName) throws Exception {
        super(new URI("ws://" + MainAPI.getInstance().getDataClassManager().getConfigManager().getIp() + ":" + MainAPI.getInstance().getDataClassManager().getConfigManager().getPort() + "/ws"));
        MCWebSocketClient.playerName = playerName;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket 클라이언트 연결 완료");
    }

    @Override
    public void onMessage(String message) {
        String[] messages = message.split("//");
        WebSocketEventHandler webSocketEventHandler = new WebSocketEventHandler(playerName);
        webSocketEventHandler.action(messages);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("연결 종료 : " + reason + " (" + code + ")");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("클라이언트 WebSocket 오류 : " + ex.getMessage());
    }

}
