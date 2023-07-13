package com.example.simplewebchat.websocket;

import com.example.simplewebchat.websocket.message.User;

import java.time.Instant;

public record Message(User user, String comment, Action action, Instant timestamp) {
}