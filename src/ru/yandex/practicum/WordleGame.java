package ru.yandex.practicum;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.yandex.practicum.exceptions.*;

public class WordleGame {
    private static final int MAX_STEPS = 6;
    private static final int WORD_LENGTH = 5;

    private final String answer;
    private final WordleDictionary dictionary;
    private final GameLogger log;

    private int stepsLeft;
    private boolean guessed;

    private final List<GuessRecord> history;
    private final Set<String> suggestedWords;

    public WordleGame(WordleDictionary dictionary, GameLogger log) {
        this(dictionary, dictionary.getRandomWord(), log);
    }

    public WordleGame(WordleDictionary dictionary, String answer, GameLogger log) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не должен быть null.");
        }
        if (log == null) {
            throw new IllegalArgumentException("Логгер не должен быть null.");
        }

        this.dictionary = dictionary;
        this.log = log;
        this.answer = dictionary.normalizeWord(answer);
        this.stepsLeft = MAX_STEPS;
        this.guessed = false;
        this.history = new ArrayList<>();
        this.suggestedWords = new HashSet<>();

        log.info("Создана новая игра. Загаданное слово подготовлено. Длина слова: " + this.answer.length());

        if (!dictionary.isValidWord(this.answer)) {
            String message = "Некорректное загаданное слово: " + this.answer;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public UserTurnResult makeTurn(String rawGuess) throws GameException {
        if (isFinished()) {
            String message = "Попытка сделать ход после завершения игры.";
            log.warning(message);
            throw new GameAlreadyFinishedException(message);
        }

        String guess = dictionary.normalizeWord(rawGuess);
        validateGuess(guess);

        stepsLeft--;

        String hint = buildHint(guess, answer);
        guessed = guess.equals(answer);

        history.add(new GuessRecord(guess, hint));

        log.info("Сделан ход. Слово: " + guess
                + ", подсказка: " + hint
                + ", осталось попыток: " + stepsLeft
                + ", победа: " + guessed);

        return new UserTurnResult(guess, hint, guessed, stepsLeft);
    }

    private void validateGuess(String guess) throws GameException {
        if (guess.isEmpty()) {
            String message = "Пустой ввод недопустим как игровой ход.";
            log.warning(message);
            throw new InvalidWordLengthException(message);
        }

        if (guess.length() != WORD_LENGTH) {
            String message = "Слово должно состоять из 5 букв: " + guess;
            log.warning(message);
            throw new InvalidWordLengthException(message);
        }

        if (!dictionary.isValidWord(guess)) {
            String message = "Слово содержит недопустимые символы: " + guess;
            log.warning(message);
            throw new InvalidWordFormatException(message);
        }

        if (!dictionary.contains(guess)) {
            String message = "Слово отсутствует в словаре: " + guess;
            log.warning(message);
            throw new WordNotFoundInDictionaryException(message);
        }
    }

    public String buildHint(String guess, String answer) {
        char[] result = {'-', '-', '-', '-', '-'};
        Map<Character, Integer> remainingLetters = new HashMap<>();

        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);

            if (guessChar == answerChar) {
                result[i] = '+';
            } else {
                remainingLetters.put(answerChar, remainingLetters.getOrDefault(answerChar, 0) + 1);
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result[i] == '+') {
                continue;
            }

            char guessChar = guess.charAt(i);
            int count = remainingLetters.getOrDefault(guessChar, 0);

            if (count > 0) {
                result[i] = '^';
                remainingLetters.put(guessChar, count - 1);
            }
        }

        return new String(result);
    }

    private boolean matchesHistory(String candidate) {
        for (GuessRecord record : history) {
            String candidateHint = buildHint(record.getGuess(), candidate);

            if (!candidateHint.equals(record.getHint())) {
                return false;
            }
        }

        return true;
    }

    public String getHintWord() {
        if (isFinished()) {
            String message = "Нельзя запросить подсказку после завершения игры.";
            log.warning(message);
            return null;
        }

        List<String> matchingWords = findMatchingWords();

        for (String word : matchingWords) {
            if (!suggestedWords.contains(word)) {
                suggestedWords.add(word);
                log.info("Выдана подсказка игроку: " + word);
                return word;
            }
        }

        log.warning("Подходящие новые слова для подсказки не найдены.");
        return null;
    }

    public List<String> findMatchingWords() {
        List<String> matchingWords = new ArrayList<>();

        for (String candidate : dictionary.getWords()) {
            if (matchesHistory(candidate)) {
                matchingWords.add(candidate);
            }
        }

        log.info("Найдено подходящих слов для подсказки: " + matchingWords.size());
        return matchingWords;
    }

    public boolean isFinished() {
        return guessed || stepsLeft == 0;
    }

    public boolean isGuessed() {
        return guessed;
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    public String getAnswer() {
        return answer;
    }

    public List<GuessRecord> getHistory() {
        return history;
    }
}