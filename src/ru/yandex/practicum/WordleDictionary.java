package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleDictionary {
    private static final int WORD_LENGTH = 5;

    private final List<String> words;
    private final Set<String> wordsSet;
    private final GameLogger log;
    private final Random random = new Random();

    public WordleDictionary(List<String> rawWords, GameLogger log) {
        this.log = log;
        this.words = new ArrayList<>();
        this.wordsSet = new HashSet<>();

        if (rawWords == null) {
            String message = "Список слов не должен быть null.";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        prepareWords(rawWords);

        if (words.isEmpty()) {
            String message = "Игровой словарь пуст после фильтрации. Исходных слов: " + rawWords.size();

            log.error(message);
            throw new IllegalStateException(message);
        }
        log.info("Словарь подготовлен. Подходящих слов: " + words.size());
    }

    private void prepareWords(List<String> rawWords) {
        for (String rawWord : rawWords) {
            String normalizedWord = normalizeWord(rawWord);

            if (isValidWord(normalizedWord) && !wordsSet.contains(normalizedWord)) {
                words.add(normalizedWord);
                wordsSet.add(normalizedWord);
            }
        }
    }

    public String normalizeWord(String word) {
        if (word == null) {
            return "";
        }

        return word.trim()
                .toLowerCase()
                .replace('ё', 'е');
    }

    public boolean isValidWord(String word) {
        if (word.isEmpty()) {
            return false;
        }

        if (word.length() != WORD_LENGTH) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (ch < 'а' || ch > 'я') {
                return false;
            }
        }

        return true;
    }

    public boolean contains(String word) {
        String normalizedWord = normalizeWord(word);
        return wordsSet.contains(normalizedWord);
    }

    public String getRandomWord() {
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public int size() {
        return words.size();
    }
}