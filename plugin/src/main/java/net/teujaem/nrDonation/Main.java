package net.teujaem.nrDonation;

import net.teujaem.nrDonation.command.ReloadCmd;
import net.teujaem.nrDonation.config.ConfigManager;
import net.teujaem.nrDonation.websoket.MCWebSocketClient;
import net.teujaem.nrDonation.websoket.MCWebSocketServerApplication;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    private static Main instance;

    private ConfigManager configManager;
    private MCWebSocketServerApplication webSocketServerApplication;
    private MCWebSocketClient mcWebSocketClient;

    @Override
    public void onEnable() {
        instance = this;
        reload();

        Objects.requireNonNull(getCommand("nrdonation-reload")).setExecutor(new ReloadCmd());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MCWebSocketServerApplication getWebSocketServerApplication() {
        return webSocketServerApplication;
    }

    public MCWebSocketClient getMcWebSocketClient() {
        return mcWebSocketClient;
    }

    public void reload() {

        configManager = new ConfigManager(this);

        if (configManager.getType().equals("main")) {
            webSocketServerApplication = new MCWebSocketServerApplication();
        }

        if (configManager.getType().equals("sub")) {
            try {
                mcWebSocketClient = new MCWebSocketClient();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
