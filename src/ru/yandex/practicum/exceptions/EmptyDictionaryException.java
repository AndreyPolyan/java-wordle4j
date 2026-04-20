package ru.yandex.practicum.exceptions;

public class EmptyDictionaryException extends RuntimeException {
    public EmptyDictionaryException(String message) {
        super(message);
    }
}
