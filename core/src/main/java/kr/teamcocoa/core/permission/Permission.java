package kr.teamcocoa.core.permission;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Permission {

    DEFAULT(0, "teamcocoa.default", Collections.EMPTY_LIST),
    BETA_TESTER(1, "teamcocoa.betatester", List.of(DEFAULT)),
    PRIME(94, "teamcocoa.prime", List.of(DEFAULT)),
    VIP(95, "teamcocoa.vip", List.of(PRIME)),
    YOUTUBE(95, "teamcocoa.youtube", List.of(VIP)),
    LEGEND(96, "teamcocoa.legend", List.of(VIP)),
    YOUTUBE_PLUS(96, "teamcocoa.youtube.plus", List.of(YOUTUBE)),
    BUILDER(97, "teamcocoa.builder", Collections.EMPTY_LIST),
    HELPER(97, "teamcocoa.helper", List.of(VIP, DEFAULT)),
    MODERATOR(6, "teamcocoa.moderator", List.of(HELPER)),
    ADMIN(99, "*", Collections.EMPTY_LIST),
    DEVELOPER(100, "*", Collections.EMPTY_LIST),
    OWNER(100, "*", Collections.EMPTY_LIST);

    private int priority;
    private String permission;
    private List<Permission> inheritence;

}
