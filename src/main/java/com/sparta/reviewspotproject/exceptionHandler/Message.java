package com.sparta.reviewspotproject.exceptionHandler;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Message {

    private String statusCode;
    private String message;

    public Message(String message, String statusCode) {
        this.statusCode = statusCode;
        this.message = message;
    }
}