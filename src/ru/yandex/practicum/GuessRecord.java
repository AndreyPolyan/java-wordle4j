package ru.yandex.practicum;

public class GuessRecord {
    private final String guess;
    private final String hint;

    public GuessRecord(String guess, String hint) {
        this.guess = guess;
        this.hint = hint;
    }

    public String getGuess() {
        return guess;
    }

    public String getHint() {
        return hint;
    }
}