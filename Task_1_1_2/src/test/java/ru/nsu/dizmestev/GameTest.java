package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    void testBlackjackScenario() {
        List<Card> cards = Arrays.asList(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.DIAMONDS, Rank.QUEEN),
                new Card(Suit.CLUBS, Rank.TWO)
        );

        String input = "0";
        Scanner scanner = new Scanner(input);
        DeckProvider provider = new FixedDeckProvider(cards);
        Game game = new Game(provider, scanner);
        game.startRound();
        assertEquals(1, game.getPlayerWins());
        assertEquals(0, game.getDealerWins());
        scanner.close();
    }

    @Test
    void testPlayerBustScenario() {
        List<Card> cards = Arrays.asList(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.DIAMONDS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.SPADES, Rank.SEVEN)
        );

        String input = "1\n"; // Игрок решает взять карту
        Scanner scanner = new Scanner(input);
        DeckProvider provider = new FixedDeckProvider(cards);
        Game game = new Game(provider, scanner);
        game.startRound();
        assertEquals(0, game.getPlayerWins());
        assertEquals(1, game.getDealerWins());
        scanner.close();
    }

    @Test
    void testDealerBustScenario() {
        List<Card> cards = Arrays.asList(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.SPADES, Rank.SIX),
                new Card(Suit.DIAMONDS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.HEARTS, Rank.TEN) // Дилер берет эту карту и перебирает
        );

        String input = "0\nq\n"; // Игрок останавливается
        Scanner scanner = new Scanner(input);
        DeckProvider provider = new FixedDeckProvider(cards);
        Game game = new Game(provider, scanner);
        game.start();
        assertEquals(1, game.getPlayerWins());
        assertEquals(0, game.getDealerWins());
        scanner.close();
    }
}