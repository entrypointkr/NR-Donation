package net.teujaem.nrDonation.clinet;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import net.teujaem.nrDonation.common.MainAPI;
import net.teujaem.nrDonation.common.data.PlatformType;
import net.teujaem.nrDonation.common.event.EventManager;
import net.teujaem.nrDonation.common.handler.donation.chzzk.ChzzkCreateCode;
import net.teujaem.nrDonation.common.handler.donation.soop.SoopCreateCode;
import net.teujaem.nrDonation.common.manager.DataClassManager;
import net.teujaem.nrDonation.common.server.CallbackServer;
import net.teujaem.nrDonation.clinet.config.ConfigManager;
import net.teujaem.nrDonation.common.websocket.sender.MCWebSocketSendMessage;
import net.teujaem.nrDonation.handler.MessageHandler;

import java.io.IOException;
import java.net.URI;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class NrDonationClient implements ClientModInitializer {

    private static NrDonationClient instance;

    private static DataClassManager dataClassManager;
    private static MessageHandler messageHandler;
    private static MainAPI mainAPI;
    private static EventManager eventManager;

    @Override
    public void onInitializeClient() {
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

        loadCommand();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {

            mainAPI = new MainAPI(MinecraftClient.getInstance().player.getName().getString());

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

            new ConfigManager();

            dataClassManager = mainAPI.getDataClassManager();

            try {
                new CallbackServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            messageHandler = new MessageHandler(MinecraftClient.getInstance().player);

            mainAPI.getDataClassManager().crateMcWebSocketClient();

        });

    }

    private void loadCommand() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            // soop eventLogin command
            dispatcher.register(literal("숲")
                .then(literal("로그인").executes(ctx -> {
                    login(PlatformType.SOOP);
                    return 1;
                }))
                .then(literal("로그아웃").executes(ctx -> {
                    logout(PlatformType.SOOP);
                    return 1;
                }))
            );

            // chzzk eventLogin command
            dispatcher.register(literal("치지직")
                .then(literal("로그인").executes(ctx -> {
                    login(PlatformType.CHZZK);
                    return 1;
                }))
                .then(literal("로그아웃").executes(ctx -> {
                    logout(PlatformType.CHZZK);
                    return 1;
                }))
            );
        });

    }

    private void login(PlatformType platformType) {
        if (platformType.equals(PlatformType.SOOP)) {
            if (isLoginReturnType(PlatformType.SOOP)) {
                return;
            }

            SoopCreateCode tokenCreate = new SoopCreateCode(dataClassManager.getApiKey().getId(PlatformType.SOOP));
            dataClassManager.getLoginPlatform().setPlatformType(PlatformType.SOOP);
            try {
                URI url = tokenCreate.getLoginUrl();
                openLoginPage(url);
            } catch (IOException | InterruptedException e) {
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

    private void logout(PlatformType platformType) {
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

    private boolean isLoginTrying() {
        return dataClassManager.getLoginPlatform().getPlatformType() != null;
    }

    private boolean isEmptyAPI(PlatformType platformType) {
        return (dataClassManager.getApiKey().getId(platformType) == null || dataClassManager.getApiKey().getSecret(platformType) == null);
    }

    private boolean isEmptyNodeJSUrl() {
        return dataClassManager.getNodeJSUrl().getURL() == null;
    }

    private boolean isLogin(PlatformType platformType) {
        if (platformType.equals(PlatformType.SOOP)) {
            return dataClassManager.getAccessToken().getSoop() != null;
        }
        if (platformType.equals(PlatformType.CHZZK)) {
            return dataClassManager.getAccessToken().getChzzk() != null;
        }
        return false;
    }

    private void openLoginPage(URI url) {
        messageHandler.loginAttempt(url);
        Util.getOperatingSystem().open(url);
    }

}
