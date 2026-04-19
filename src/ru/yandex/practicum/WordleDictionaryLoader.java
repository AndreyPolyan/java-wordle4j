package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final GameLogger log;

    public WordleDictionaryLoader(GameLogger log) {
        this.log = log;
    }

    public WordleDictionary load(String fileName) throws IOException {
        log.info("Начинаем загрузку словаря из файла: " + fileName);

        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fileName, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }

            log.info("Словарь успешно загружен. Исходных строк: " + words.size());
            return new WordleDictionary(words, log);

        } catch (IOException e) {
            log.error("Ошибка при загрузке словаря: " + e.getMessage());
            throw e;
        }
    }
}