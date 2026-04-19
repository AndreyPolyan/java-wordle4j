package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import ru.yandex.practicum.exceptions.*;

public class Wordle {
    private static final String LOG_FILE_NAME = "wordle.log";
    private static final String DICTIONARY_FILE_NAME = "words_ru.txt";

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_NAME, true));
             Scanner scanner = new Scanner(System.in)) {

            GameLogger log = new GameLogger(writer);

            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.load(DICTIONARY_FILE_NAME);
            WordleGame game = new WordleGame(dictionary, log);

            System.out.println("Игра Wordle началась!");
            System.out.println("Нужно угадать существительное из 5 букв.");
            System.out.println("У вас есть 6 попыток.");

            while (!game.isFinished()) {
                System.out.println();
                System.out.println("Осталось попыток: " + game.getStepsLeft());
                System.out.print("Введите слово: ");

                String input = scanner.nextLine();

                if (input.isBlank()) {
                    String suggestedWord = game.getHintWord();

                    if (suggestedWord == null) {
                        System.out.println("Подходящих новых слов для подсказки не найдено.");
                    } else {
                        System.out.println("Подсказка: " + suggestedWord);
                    }

                    continue;
                }

                try {
                    UserTurnResult result = game.makeTurn(input);
                    System.out.println(result.getGuess());
                    System.out.println(result.getHint());

                    if (result.isGuessed()) {
                        System.out.println("Вы угадали слово!");
                    }
                } catch (InvalidWordLengthException e) {
                    System.out.println("Ошибка: слово должно состоять из 5 букв.");
                } catch (InvalidWordFormatException e) {
                    System.out.println("Ошибка: используйте только русские буквы.");
                } catch (WordNotFoundInDictionaryException e) {
                    System.out.println("Ошибка: такого слова нет в словаре.");
                } catch (GameAlreadyFinishedException e) {
                    System.out.println("Игра уже завершена.");
                }
            }

            System.out.println();
            if (!game.isGuessed()) {
                System.out.println("Попытки закончились.");
            }
            System.out.println("Загаданное слово: " + game.getAnswer());

            log.info("Игра завершена. Победа: " + game.isGuessed()
                    + ", оставшиеся попытки: " + game.getStepsLeft()
                    + ", ответ: " + game.getAnswer());

        } catch (IOException e) {
            System.out.println("Не удалось открыть лог-файл или загрузить словарь.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Произошла фатальная ошибка.");
            e.printStackTrace();
        }
    }
}