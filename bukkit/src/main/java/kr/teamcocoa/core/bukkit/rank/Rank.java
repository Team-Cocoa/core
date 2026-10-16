package kr.teamcocoa.core.bukkit.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Rank {

    private final String name;

    /**
     * Lower is higher in tab and wins when a player has several ranks.
     */
    private final int sortId;

    private final String permission;

    private final String prefix;

    private final String display;

    private final String suffix;

}
