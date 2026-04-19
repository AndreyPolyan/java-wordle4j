package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private static GameLogger log;

    @BeforeAll
    static void beforeAll() {
        log = new GameLogger(new PrintWriter(System.out, true));
    }

    @Test
    void shouldNormalizeWordToLowerCaseAndReplaceYo() {
        WordleDictionary dictionary = new WordleDictionary(List.of("ЁЖИК", "Арбуз"), log);

        assertEquals("ежик", dictionary.normalizeWord("ЁЖИК"));
        assertEquals("елка", dictionary.normalizeWord("  ЁлКа "));
    }

    @Test
    void shouldKeepOnlyValidRussianFiveLetterWords() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("Арбуз", "дом", "abcde", "12345", "ёжик", "герой"),
                log
        );

        assertEquals(2, dictionary.size());
        assertTrue(dictionary.contains("арбуз"));
        assertTrue(dictionary.contains("герой"));
        assertFalse(dictionary.contains("ежик"));
        assertFalse(dictionary.contains("дом"));
        assertFalse(dictionary.contains("abcde"));
    }

    @Test
    void shouldRemoveDuplicatesAfterNormalization() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("актёр", "Актер", " АКТЁР "),
                log
        );
        assertEquals(1, dictionary.size());
        assertTrue(dictionary.contains("актёр"));
        assertTrue(dictionary.contains("Актер"));
    }

    @Test
    void containsShouldUseNormalizedWord() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("актёр", "Герой"),
                log
        );

        assertTrue(dictionary.contains("актер"));
        assertTrue(dictionary.contains("АКТЁР"));
        assertTrue(dictionary.contains("герой"));
        assertFalse(dictionary.contains("домик"));
    }

    @Test
    void shouldThrowExceptionWhenDictionaryBecomesEmptyAfterFiltering() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WordleDictionary(List.of("дом", "123", "abc"), log)
        );

        assertEquals("Игровой словарь пуст после фильтрации. Исходных слов: 3", exception.getMessage());
    }
}