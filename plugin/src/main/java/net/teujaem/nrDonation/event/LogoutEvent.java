package net.teujaem.nrDonation.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LogoutEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String flatform;
    private final Player player;

    public LogoutEvent(String flatform, Player player) {
        this.flatform = flatform;
        this.player = player;
    }

    public String getFlatform() {
        return this.flatform;
    }

    public Player getPlayer() {
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
