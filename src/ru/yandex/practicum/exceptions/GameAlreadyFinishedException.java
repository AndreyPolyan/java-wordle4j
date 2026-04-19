package ru.yandex.practicum.exceptions;

public class GameAlreadyFinishedException extends GameException {
    public GameAlreadyFinishedException(String message) {
        super(message);
    }
}