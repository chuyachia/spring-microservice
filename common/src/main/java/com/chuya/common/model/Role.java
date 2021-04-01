package com.chuya.common.model;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN("ADMIN", Arrays.asList("users.read","users.write","departments.read","departments.write")),
    USER("USER", Arrays.asList("users.read","departments.read"));

    public final String name;
    public final List<String> authorities;

    Role(String name, List<String> authorities) {
        this.name = name;
        this.authorities = authorities;
    }
}
