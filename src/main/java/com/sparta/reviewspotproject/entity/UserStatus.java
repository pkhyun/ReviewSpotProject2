package com.sparta.reviewspotproject.entity;

public enum UserStatus {
    MEMBER("정상 회원"),
    NON_MEMBER("탈퇴 회원");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}