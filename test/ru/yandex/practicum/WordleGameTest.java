package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import ru.yandex.practicum.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private static GameLogger log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeAll
    static void beforeAll() {
        log = new GameLogger(new PrintWriter(System.out, true));
    }

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(
                List.of(
                        "герой",
                        "гонец",
                        "ангар",
                        "казак",
                        "нагар",
                        "ежик",
                        "елка",
                        "шпик"
                ),
                log
        );
        game = new WordleGame(dictionary, "герой", log);
    }

    @Test
    void shouldStartWithSixSteps() {
        assertEquals(6, game.getStepsLeft());
        assertFalse(game.isFinished());
        assertFalse(game.isGuessed());
    }

    @Test
    void validTurnShouldDecreaseSteps() throws GameException {
        UserTurnResult result = game.makeTurn("гонец");

        assertEquals(5, game.getStepsLeft());
        assertEquals(5, result.getStepsLeft());
        assertEquals("гонец", result.getGuess());
        assertEquals("+^-^-", result.getHint());
    }

    @Test
    void winningTurnShouldMarkGameAsGuessed() throws GameException {
        UserTurnResult result = game.makeTurn("герой");

        assertTrue(result.isGuessed());
        assertTrue(game.isGuessed());
        assertTrue(game.isFinished());
        assertEquals(5, game.getStepsLeft());
    }

    @Test
    void invalidLengthShouldNotDecreaseSteps() {
        assertThrows(InvalidWordLengthException.class, () -> game.makeTurn("дом"));
        assertEquals(6, game.getStepsLeft());
    }

    @Test
    void emptyInputShouldNotDecreaseSteps() {
        assertThrows(InvalidWordLengthException.class, () -> game.makeTurn(""));
        assertEquals(6, game.getStepsLeft());
    }

    @Test
    void invalidFormatShouldNotDecreaseSteps() {
        assertThrows(InvalidWordFormatException.class, () -> game.makeTurn("abcde"));
        assertEquals(6, game.getStepsLeft());
    }

    @Test
    void wordNotFromDictionaryShouldNotDecreaseSteps() {
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeTurn("буква"));
        assertEquals(6, game.getStepsLeft());
    }

    @Test
    void shouldFinishAfterSixValidTurns() throws GameException {
        WordleGame losingGame = new WordleGame(dictionary, "герой", log);

        for (int i = 0; i < 6; i++) {
            losingGame.makeTurn("гонец");
        }

        assertTrue(losingGame.isFinished());
        assertFalse(losingGame.isGuessed());
        assertEquals(0, losingGame.getStepsLeft());
    }

    @Test
    void shouldThrowWhenTurnMadeAfterGameFinished() throws GameException {
        game.makeTurn("герой");

        assertThrows(GameAlreadyFinishedException.class, () -> game.makeTurn("гонец"));
    }

    @Test
    void buildHintShouldHandleRepeatedLettersCorrectly() {
        String hint = game.buildHint("казак", "ангар");

        assertEquals("-^-+-", hint);
    }

    @Test
    void historyShouldStoreValidTurnsOnly() throws GameException {
        game.makeTurn("гонец");
        game.makeTurn("герой");

        assertEquals(2, game.getHistory().size());
        assertEquals("гонец", game.getHistory().get(0).getGuess());
        assertEquals("герой", game.getHistory().get(1).getGuess());
    }

    @Test
    void invalidTurnShouldNotBeSavedToHistory() {
        assertThrows(InvalidWordLengthException.class, () -> game.makeTurn("дом"));
        assertEquals(0, game.getHistory().size());
    }

    @Test
    void gameShouldNormalizeUserInput() throws GameException {
        WordleGame localGame = new WordleGame(
                new WordleDictionary(List.of("актёр", "герой"), log),
                "актер",
                log
        );

        UserTurnResult result = localGame.makeTurn("АКТЁР");

        assertTrue(result.isGuessed());
    }

    @Test
    void getHintWordShouldReturnWordWhenHistoryIsEmpty() {
        String hintWord = game.getHintWord();

        assertNotNull(hintWord);
        assertTrue(dictionary.contains(hintWord));
    }

    @Test
    void getHintWordShouldReturnCompatibleCandidate() throws GameException {
        WordleGame hintGame = new WordleGame(dictionary, "герой", log);
        hintGame.makeTurn("гонец");

        String hintWord = hintGame.getHintWord();

        assertNotNull(hintWord);
        assertEquals(
                hintGame.buildHint("гонец", "герой"),
                hintGame.buildHint("гонец", hintWord)
        );
    }

    @Test
    void getHintWordShouldNotRepeatSuggestion() throws GameException {
        WordleGame hintGame = new WordleGame(
                new WordleDictionary(List.of("ангар", "нагар", "казак", "шпик", "герой"), log),
                "ангар",
                log
        );

        hintGame.makeTurn("казак");

        String firstHint = hintGame.getHintWord();
        String secondHint = hintGame.getHintWord();

        if (secondHint != null) {
            assertNotEquals(firstHint, secondHint);
        }
    }
}