package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {
    private static GameLogger log;

    @BeforeAll
    static void beforeAll() {
        log = new GameLogger(new PrintWriter(System.out, true));
    }

    @Test
    void shouldLoadWordsFromUtf8File(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("words_ru.txt");
        Files.writeString(
                file,
                "герой\nактёр\nдом\nabcde\n",
                StandardCharsets.UTF_8
        );

        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        WordleDictionary dictionary = loader.load(file.toString());

        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("актер"));
        assertFalse(dictionary.contains("дом"));
        assertEquals(2, dictionary.size());
    }
}