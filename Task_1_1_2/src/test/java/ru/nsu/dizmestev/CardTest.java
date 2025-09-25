package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void testCardValue() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        assertEquals(11, card.getValue());
    }

    @Test
    void testCardToString() {
        Card card = new Card(Suit.SPADES, Rank.QUEEN);
        assertEquals("Дама Пики (10)", card.toString());
    }
}