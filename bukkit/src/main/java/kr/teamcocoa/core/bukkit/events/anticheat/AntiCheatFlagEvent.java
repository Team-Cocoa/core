package kr.teamcocoa.core.bukkit.events.anticheat;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class AntiCheatFlagEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private Player player;
    private AntiCheatFlag flag;
    private double tps;

    public AntiCheatFlagEvent(Player player, AntiCheatFlag flag, double tps) {
        super(true);
        this.player = player;
        this.flag = flag;
        this.tps = tps;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
