package net.teujaem.nrDonation.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.teujaem.nrDonation.NrDonation;
import net.teujaem.nrDonation.NrDonationCommands;
import net.teujaem.nrDonation.client.config.ConfigManager;
import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.data.PlatformType;
import net.teujaem.nrDonation.common.event.EventManager;
import net.teujaem.nrDonation.common.handler.donation.chzzk.ChzzkCreateCode;
import net.teujaem.nrDonation.common.handler.donation.soop.SoopCreateCode;
import net.teujaem.nrDonation.common.manager.DataClassManager;
import net.teujaem.nrDonation.common.server.CallbackServer;
import net.teujaem.nrDonation.common.websocket.sender.MCWebSocketSendMessage;
import net.teujaem.nrDonation.handler.MessageHandler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NrDonationClient {
    private static NrDonationClient instance;

    private static DataClassManager dataClassManager;
    private static MessageHandler messageHandler;
    private static MainAPI mainAPI;
    private static EventManager eventManager;
    private List<Runnable> tasks = new ArrayList<>();

    public NrDonationClient() {
        instance = this;
        load();
    }

    public static NrDonationClient getInstance() {
        return instance;
    }

    public MainAPI getMainAPI() {
        return mainAPI;
    }

    private void load() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            ArrayList<Runnable> tasks = new ArrayList<>(this.tasks);
            this.tasks.clear();
            for (Runnable task : tasks) {
                try {
                    task.run();
                } catch (Exception ex) {
                    NrDonation.getLogger().warn("Error while executing task", ex);
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientPlayerNetworkEvent.LoggingIn event) {
        scheduleInitializeOnJoin();
    }

    @SubscribeEvent
    public void onClientCommand(RegisterClientCommandsEvent e) {
        NrDonationCommands.register(this, e.getDispatcher());
    }

    private void scheduleInitializeOnJoin() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            initializeOnJoin(player);
        } else {
            tasks.add(this::scheduleInitializeOnJoin);
        }
    }

    private void initializeOnJoin(Player player) {
        mainAPI = new MainAPI(player.getGameProfile().getName());

        // 로그인 감지를 위한 interface 추가
        eventManager = mainAPI.getEventManager();

        eventManager.addListener(message -> {
            String[] messages = message.split("//");
            String event = messages[0];
            String platform = messages[1];
            if (event.equals("login")) {
                messageHandler.loginSuccess();
                MCWebSocketSendMessage mcWebSocketSendMessage = new MCWebSocketSendMessage();
                mcWebSocketSendMessage.to("event//login//" + platform);
            }
            if (event.equals("loginTry")) {
                if (platform.equals("soop")) login(PlatformType.SOOP);
                if (platform.equals("chzzk")) login(PlatformType.CHZZK);
            }
        });

        // 마인크래프트 config 로딩
        new ConfigManager(Minecraft.getInstance().gameDirectory);

        // 메인 시스템에 data 관리 class 로딩
        dataClassManager = mainAPI.getDataClassManager();

        // 숲, 치지직 연동을 위한 callback 서버 실행
        try {
            new CallbackServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 메세지를 보내기 위한 handler class 로딩
        messageHandler = new MessageHandler(player);

        // 마크 서버와 연동할 ws 로딩
        mainAPI.getDataClassManager().crateMcWebSocketClient();
    }

    // 로그인 시도
    public void login(PlatformType platformType) {
        if (platformType.equals(PlatformType.SOOP)) {
            if (isLoginReturnType(PlatformType.SOOP)) {
                return;
            }

            SoopCreateCode tokenCreate = new SoopCreateCode(dataClassManager.getApiKey().getId(PlatformType.SOOP));
            dataClassManager.getLoginPlatform().setPlatformType(PlatformType.SOOP);
            try {
                URI url = tokenCreate.getLoginUrl();
                openLoginPage(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (platformType.equals(PlatformType.CHZZK)) {
            if (isLoginReturnType(PlatformType.CHZZK)) {
                return;
            }

            ChzzkCreateCode tokenCreate = new ChzzkCreateCode(dataClassManager.getApiKey().getId(PlatformType.CHZZK));
            dataClassManager.getLoginPlatform().setPlatformType(PlatformType.CHZZK);
            URI url = tokenCreate.getLoginUrl();
            openLoginPage(url);
        }
    }

    // 로그아웃 시도
    public void logout(PlatformType platformType) {
        if (!isLogin(platformType)) {
            messageHandler.alreadyLogout();
            return;
        }

        dataClassManager.getLoginPlatform().setPlatformType(null);
        dataClassManager.getAccessToken().reset(platformType);

        if (platformType.equals(PlatformType.SOOP)) {
            if (dataClassManager.getSoopClient().getLatch() != null) {
                dataClassManager.getSoopClient().stop();
            }
            return;
        }
        if (platformType.equals(PlatformType.CHZZK)) {
            if (dataClassManager.getChzzkClient().getSocket() != null) {
                dataClassManager.getChzzkClient().stop();
            }
        }

        messageHandler.logoutSuccess();
        MCWebSocketSendMessage mcWebSocketSendMessage = new MCWebSocketSendMessage();
        mcWebSocketSendMessage.to("event//logout//" + platformType.toString().toLowerCase());
    }

    // 로그인 시도 실패이유
    private boolean isLoginReturnType(PlatformType platformType) {
        if (isLogin(platformType)) {
            messageHandler.alreadyLogin();
            return true;
        }
        if (isLoginTrying()) {
            messageHandler.loginTrying();
            return true;
        }
        if (isEmptyAPI(platformType)) {
            messageHandler.emptyAPI();
            return true;
        }
        if (platformType.equals(PlatformType.SOOP)) {
            if (isEmptyNodeJSUrl()) {
                messageHandler.emptyNodeJSUrl();
                return true;
            }
        }
        return false;
    }

    // 로그인중인지 확인
    private boolean isLoginTrying() {
        return dataClassManager.getLoginPlatform().getPlatformType() != null;
    }

    // api key가 전달 되었는지 확인
    private boolean isEmptyAPI(PlatformType platformType) {
        return (dataClassManager.getApiKey().getId(platformType) == null || dataClassManager.getApiKey().getSecret(platformType) == null);
    }

    // nodejs 서버가 전달 되었는지 확인
    private boolean isEmptyNodeJSUrl() {
        return dataClassManager.getNodeJSUrl().getURL() == null;
    }

    // 로그인 상태인지 감지
    private boolean isLogin(PlatformType platformType) {
        if (platformType.equals(PlatformType.SOOP)) {
            return dataClassManager.getAccessToken().getSoop() != null;
        }
        if (platformType.equals(PlatformType.CHZZK)) {
            return dataClassManager.getAccessToken().getChzzk() != null;
        }
        return false;
    }

    // 로그인 페이지 열기
    private void openLoginPage(URI url) {
        messageHandler.loginAttempt(url);
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(url);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
