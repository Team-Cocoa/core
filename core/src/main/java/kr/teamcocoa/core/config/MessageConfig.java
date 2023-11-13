package kr.teamcocoa.core.config;

import kr.teamcocoa.core.utils.StringUtils;

public class MessageConfig {

    public static final String PREFIX = StringUtils.color("&a[&dTeamCocoa&a] &f");
    public static final String NO_PERMISSION = PREFIX +
            StringUtils.color("&7This command does not exist or is deactivated.");
    public static final String INVALID_COMMAND = NO_PERMISSION;

}
