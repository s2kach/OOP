package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;


class DealerTest {

    @Test
    void testDealerShouldTakeCard() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        // 10 < 17 - должен брать карту
        assertTrue(dealer.shouldTakeCard());
    }

    @Test
    void testDealerShouldStop() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.takeCard(new Card(Suit.SPADES, Rank.SEVEN));
        // 10 + 7 = 17 - должен остановиться
        assertFalse(dealer.shouldTakeCard());
    }
}