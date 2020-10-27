package jy.learning.bootrestapi.accounts;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum AccountRole {
    ADMIN,

    USER;

    public static Set<AccountRole> makeSetRoles(AccountRole... roles) {
        return Arrays.stream(roles).collect(Collectors.toSet());
    }
}
