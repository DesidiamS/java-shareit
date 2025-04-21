package ru.practicum.shareit.exceptions;

public class UserNotPresented extends RuntimeException {
    String message;

    public UserNotPresented(String message) {
        this.message = message;
    }
}
