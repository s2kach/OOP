package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HandTest {

    @Test
    void testBlackjackDetection() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.KING));
        assertTrue(hand.hasBlackjack());
    }

    @Test
    void testAceValueAdjustment() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
        // ACE(11) + ACE(1) + NINE(9) = 21
        assertEquals(21, hand.calculateScore());
    }

    @Test
    void testBustDetection() {
        Hand hand = new Hand();
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.JACK));
        // 10 + 10 + 10 = 30 (>21)
        assertEquals(30, hand.calculateScore());
    }
}