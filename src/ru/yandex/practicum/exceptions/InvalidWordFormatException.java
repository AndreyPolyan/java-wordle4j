package ru.yandex.practicum.exceptions;

public class InvalidWordFormatException extends GameException {
    public InvalidWordFormatException(String message) {
        super(message);
    }
}