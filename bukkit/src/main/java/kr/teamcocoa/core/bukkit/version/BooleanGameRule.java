package kr.teamcocoa.core.bukkit.version;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BooleanGameRule {

    DAYLIGHT_CYCLE("doDaylightCycle"),
    ANNOUNCE_ADVANCEMENTS("announceAdvancements"),
    LOG_ADMIN_COMMANDS("logAdminCommands");

    private final String legacyName;

}
