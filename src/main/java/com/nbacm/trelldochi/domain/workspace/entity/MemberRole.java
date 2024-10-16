package com.nbacm.trelldochi.domain.workspace.entity;

import com.nbacm.trelldochi.domain.common.exception.BadRequestException;

import java.util.Arrays;

public enum MemberRole {
    ADMIN, MEMBER, READONLY;

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("유효하지 않은 UserRole"));
    }
}
