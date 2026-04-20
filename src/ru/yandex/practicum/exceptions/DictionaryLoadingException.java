package ru.yandex.practicum.exceptions;

public class DictionaryLoadingException extends RuntimeException {
    public DictionaryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
