package ru.practicum.shareit.exceptions;

public class WrongOwnerException extends RuntimeException {
    public WrongOwnerException(String message) {
        super(message);
    }
}
