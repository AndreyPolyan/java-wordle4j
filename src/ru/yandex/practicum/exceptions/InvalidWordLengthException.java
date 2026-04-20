package ru.yandex.practicum.exceptions;

public class InvalidWordLengthException extends GameException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
}