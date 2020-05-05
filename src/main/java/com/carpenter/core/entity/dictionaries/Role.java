package com.carpenter.core.entity.dictionaries;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    ADMINISTRATOR("ADMINISTRATOR"),
    MANAGER("MANAGER"),
    EMPLOYER("EMPLOYER");

    private final String roleType;

    private static Map<String, Role> roleStringToEnum;

    Role(String roleType) {
        this.roleType = roleType;
    }

    public static Role getRole(String role) {
        if (roleStringToEnum == null) {
            initRoles();
        }
        return roleStringToEnum.get(role);
    }

    public static void initRoles(){
        roleStringToEnum = Stream.of(Role.values())
                .collect(Collectors.toMap(r -> r.roleType, Function.identity()));
    }
}
