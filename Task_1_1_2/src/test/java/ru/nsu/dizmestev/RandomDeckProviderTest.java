package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class RandomDeckProviderTest {

    @Test
    void testSingleDeckProvider() {
        RandomDeckProvider provider = new RandomDeckProvider(1);
        Deck deck = provider.getDeck();
        assertNotNull(deck);
        assertEquals(52, deck.size());
    }

    @Test
    void testMultipleDecksProvider() {
        RandomDeckProvider provider = new RandomDeckProvider(4);
        Deck deck = provider.getDeck();
        assertNotNull(deck);
        assertEquals(208, deck.size()); // 52 * 4
    }

    @Test
    void testMaximumDecksProvider() {
        RandomDeckProvider provider = new RandomDeckProvider(8);
        Deck deck = provider.getDeck();
        assertNotNull(deck);
        assertEquals(416, deck.size()); // 52 * 8
    }

    @Test
    void testZeroDecksProvider() {
        RandomDeckProvider provider = new RandomDeckProvider(0);
        Deck deck = provider.getDeck();
        assertNotNull(deck);
        assertEquals(0, deck.size());
    }

    @Test
    void testNegativeDecksProvider() {
        // Ожидаем исключение при создании Deck с отрицательным количеством колод
        RandomDeckProvider provider = new RandomDeckProvider(-1);
        assertThrows(IllegalStateException.class, () -> {
            Deck deck = provider.getDeck();
            deck.drawCard(); // Попытка вытащить карту из пустой колоды
        });
    }
}