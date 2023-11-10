package kr.teamcocoa.core.bukkit.events.anticheat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AntiCheatFlag {

    private String name;
    private String type;
    private int vl;
    private int maxVl;

}
