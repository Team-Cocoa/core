package kr.teamcocoa.core.bukkit.events.anticheat;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class AntiCheatPunishEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();


    private Player player;

    public AntiCheatPunishEvent(Player player) {
        super(true);
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
