package net.teujaem.nrDonation.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {
    private final File file;

    private String host;
    private int port;
    private String type;
    private String soopId;
    private String soopSecret;
    private String chzzkId;
    private String chzzkSecret;
    private String soopNodejs;


    public ConfigManager(JavaPlugin plugin) {

        file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }

        reloadConfig();
    }

    public void reloadConfig() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // config 로드
        host = config.getString("Server.host", "127.0.0.1");
        port = config.getInt("Server.port", 8888);
        type = config.getString("Server.type", "main");
        soopId = config.getString("APIKey.Soop.id");
        soopSecret = config.getString("APIKey.Soop.secret");
        chzzkId = config.getString("APIKey.Chzzk.id");
        chzzkSecret = config.getString("APIKey.Chzzk.secret");
        soopNodejs = config.getString("APIServer.Soop.nodejs");

    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getType() {
        return type;
    }

    public String getSoopId() {
        return soopId;
    }

    public String getSoopSecret() {
        return soopSecret;
    }

    public String getChzzkId() {
        return chzzkId;
    }

    public String getChzzkSecret() {
        return chzzkSecret;
    }

    public String getSoopNodejs() {
        return soopNodejs;
    }
}
