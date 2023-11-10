package kr.teamcocoa.core.permission;

import java.util.Arrays;

public class PermissionValidator {

    public static boolean validate(Permission userPermission, Permission required) {
        return validate(userPermission, required.getPermission());
    }

    public static boolean validate(Permission userPermission, String rawPermission) {
        if(userPermission.getPermission().equals(rawPermission)) {
            return true;
        }
        boolean find = false;
        for (Permission permission : userPermission.getInheritence()) {
            if(!find) {
                find = validate(permission, rawPermission);
            }
        }
        return find;
    }

}
