package ru.yandex.practicum;

public class UserTurnResult {
    private final String guess;
    private final String hint;
    private final boolean guessed;
    private final int stepsLeft;

    public UserTurnResult(String guess, String hint, boolean guessed, int stepsLeft) {
        this.guess = guess;
        this.hint = hint;
        this.guessed = guessed;
        this.stepsLeft = stepsLeft;
    }

    public String getGuess() {
        return guess;
    }

    public String getHint() {
        return hint;
    }

    public boolean isGuessed() {
        return guessed;
    }

    public int getStepsLeft() {
        return stepsLeft;
    }
}
