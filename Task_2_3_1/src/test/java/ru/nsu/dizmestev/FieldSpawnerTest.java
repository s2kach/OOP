package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class FieldSpawnerTest {

    @Test
    void testFindEmptyPoint() {
        FieldSpawner spawner = new FieldSpawner(2, 2);
        Snake snake = new Snake(0, 0);

        List<Point> obstacles = List.of(new Point(1, 0), new Point(0, 1));
        List<Food> foods = new ArrayList<>();

        Point empty = spawner.findEmptyPoint(snake, obstacles, foods);

        assertEquals(1, empty.getX());
        assertEquals(1, empty.getY());
    }

    @Test
    void testGenerateObstaclesAvoidsStartLine() {
        FieldSpawner spawner = new FieldSpawner(10, 10);
        int startX = 5;
        int startY = 5;
        Snake snake = new Snake(startX, startY);

        List<Point> obstacles = spawner.generateObstacles(90, snake);

        assertEquals(90, obstacles.size());
        for (Point p : obstacles) {
            if (p.getY() == startY) {
                assertFalse(p.getX() >= startX);
            }
        }
    }

    @Test
    void testSpawnFoodCreatesObjectInEmptySpace() {
        FieldSpawner spawner = new FieldSpawner(10, 10);
        Snake snake = new Snake(5, 5);
        List<Point> obstacles = new ArrayList<>();
        List<Food> currentFoods = new ArrayList<>();

        Food newFood = spawner.spawnFood(snake, obstacles, currentFoods);

        assertNotNull(newFood);
        Point pos = newFood.getPosition();

        assertTrue(pos.getX() >= 0 && pos.getX() < 10);
        assertTrue(pos.getY() >= 0 && pos.getY() < 10);

        assertFalse(snake.getBody().contains(pos));
    }
}