package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SnakeTest {

    @Test
    void testMove() {
        Snake snake = new Snake(5, 5);
        Point initialHead = snake.getBody().getFirst();

        snake.setDirection(Direction.DOWN);
        snake.move();

        Point newHead = snake.getBody().getFirst();
        assertEquals(initialHead.getX(), newHead.getX());
        assertEquals(initialHead.getY() + 1, newHead.getY());
    }

    @Test
    void testGrow() {
        Snake snake = new Snake(5, 5);
        int initialSize = snake.getBody().size();

        snake.grow();
        snake.move();

        assertEquals(initialSize + 1, snake.getBody().size());
    }

    @Test
    void testOppositeDirectionProtection() {
        Snake snake = new Snake(5, 5);
        snake.setDirection(Direction.LEFT);

        assertEquals(Direction.RIGHT, snake.getCurrentDirection());
    }

    @Test
    void testSelfCollision() {
        Snake snake = new Snake(10, 10);

        snake.grow();
        snake.move();
        snake.grow();
        snake.move();

        snake.setDirection(Direction.DOWN);
        snake.move();
        snake.setDirection(Direction.LEFT);
        snake.move();
        snake.setDirection(Direction.UP);
        snake.move();

        assertTrue(snake.checkSelfCollision());
    }

    @Test
    void testNoSelfCollision() {
        Snake snake = new Snake(10, 10);
        assertFalse(snake.checkSelfCollision());
    }
}