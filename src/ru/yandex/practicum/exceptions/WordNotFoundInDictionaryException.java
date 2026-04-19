package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionaryException extends GameException {
    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}