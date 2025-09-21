package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeckTest {

    @Test
    void testDeckCreation() {
        Deck deck = new Deck(2);
        // 52 карты * 2 колоды = 104 карты
        assertEquals(104, deck.size());
    }

    @Test
    void testDeckCreation2() {
        Deck deck = new Deck(8);
        assertEquals(52 * 8, deck.size());
    }

    @Test
    void testDrawCard() {
        Deck deck = new Deck(1);
        Card card = deck.drawCard();
        assertNotNull(card);
        assertEquals(51, deck.size());
    }

    @Test
    void testDrawFromEmptyDeck() {
        Deck deck = new Deck(1);
        // Достаем все карты
        for (int i = 0; i < 52; i++) {
            deck.drawCard();
        }

        assertThrows(IllegalStateException.class, deck::drawCard);
    }
}