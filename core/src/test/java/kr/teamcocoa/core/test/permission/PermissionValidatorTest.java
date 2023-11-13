package kr.teamcocoa.core.test.permission;

import kr.teamcocoa.core.permission.Permission;
import kr.teamcocoa.core.permission.PermissionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermissionValidatorTest {

    @Test
    @DisplayName("퍼미션 테스트")
    void 권한_테스트() {
        assertEquals(true, PermissionValidator.validate(Permission.MODERATOR, Permission.HELPER));
        assertEquals(true, PermissionValidator.validate(Permission.MODERATOR, Permission.LEGEND));
        assertEquals(true, PermissionValidator.validate(Permission.MODERATOR, Permission.DEFAULT));
    }



}