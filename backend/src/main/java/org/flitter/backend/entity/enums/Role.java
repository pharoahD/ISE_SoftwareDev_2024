package org.flitter.backend.entity.enums;

import lombok.Getter;

//@Getter
//public enum Role {
//    ADMIN(4),
//    APARTMENT_LEADER(3),
//    PROJECT_LEADER(2),
//    USER(1);
//
//    private final int priority;
//
//    Role(int priority) {
//        this.priority = priority;
//    }
//
//    public int getPriority() {
//        return priority;
//    }
//
//    public boolean isHigherThan(Role other) {
//        return this.priority > other.priority;
//    }
//}

public enum Role {
    ADMIN,
    APARTMENT_LEADER,
    PROJECT_LEADER,
    USER;
}