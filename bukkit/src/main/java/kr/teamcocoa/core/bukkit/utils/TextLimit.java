package kr.teamcocoa.core.bukkit.utils;

import kr.teamcocoa.core.bukkit.main.BukkitCoreBootstrap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TextLimit {

    TEAM_NAME(16),
    TEAM_AFFIX(16),
    SCOREBOARD_ENTRY(40),
    OBJECTIVE_TITLE(32),
    INVENTORY_TITLE(32);

    private final int legacyLength;

    public String truncate(String text) {
        if (text.length() <= legacyLength || !BukkitCoreBootstrap.getVersionAdapter().hasLegacyTextLimits()) {
            return text;
        }
        String truncated = text.substring(0, legacyLength);
        if (truncated.charAt(truncated.length() - 1) == '§') {
            truncated = truncated.substring(0, truncated.length() - 1);
        }
        return truncated;
    }

}
