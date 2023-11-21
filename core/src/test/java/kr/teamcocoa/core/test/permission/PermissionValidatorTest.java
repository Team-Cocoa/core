package kr.teamcocoa.core.test.permission;

import kr.teamcocoa.core.permission.Permission;
import kr.teamcocoa.core.permission.PermissionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("유저 권한 테스트")
class PermissionValidatorTest {

    @Test
    @DisplayName("맞는 권한 테스트")
    void 맞는_권한_테스트() {
        assertTrue(PermissionValidator.validate(Permission.OWNER, Permission.LEGEND));
        assertTrue(PermissionValidator.validate(Permission.MODERATOR, Permission.HELPER));
        assertTrue(PermissionValidator.validate(Permission.MODERATOR, Permission.LEGEND));
        assertTrue(PermissionValidator.validate(Permission.MODERATOR, Permission.DEFAULT));
    }

    @Test
    @DisplayName("안 맞는 권한 테스트")
    void 안_맞는_권한_테스트() {
        assertFalse(PermissionValidator.validate(Permission.DEFAULT, Permission.PRIME));
    }



}