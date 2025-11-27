package net.teujaem.nrDonation.common.config;

public class ConfigManager {

    private String ip;
    private int port;
    private boolean donation;
    private boolean chat;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean getDonation() {
        return donation;
    }

    public boolean getChat() {
        return chat;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDonation(boolean donation) {
        this.donation = donation;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }
}
