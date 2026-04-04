package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void testEquals() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(10, 20);
        Point p3 = new Point(5, 5);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotNull(p1);
    }

    @Test
    void testHashCode() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(10, 20);

        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
