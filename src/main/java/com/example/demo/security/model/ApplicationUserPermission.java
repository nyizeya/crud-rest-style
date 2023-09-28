package com.example.demo.security.model;

public enum ApplicationUserPermission {
    INSTRUCTOR_READ("instructor:read"),
    INSTRUCTOR_WRITE("instructor:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
