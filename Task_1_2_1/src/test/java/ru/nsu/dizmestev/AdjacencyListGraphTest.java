package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class AdjacencyListGraphTest {

    @Test
    void testAddAndRemoveVertex() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        assertEquals(List.of(), g.getNeighbors("A"));
        g.removeVertex("B");
        assertThrows(GraphException.class, () -> g.getNeighbors("B"));
    }

    @Test
    void testAddAndRemoveEdge() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        assertEquals(List.of("B"), g.getNeighbors("A"));
        g.removeEdge("A", "B");
        assertEquals(List.of(), g.getNeighbors("A"));
    }

    @Test
    void testTopologicalSort() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        List<String> sorted = g.topologicalSort();
        assertEquals(List.of("A", "B", "C"), sorted);
    }

    @Test
    void testCycleDetection() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        g.addEdge("B", "A");
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testEquals() throws Exception {
        AdjacencyListGraph g1 = new AdjacencyListGraph();
        AdjacencyListGraph g2 = new AdjacencyListGraph();
        g1.addVertex("A");
        g2.addVertex("A");
        assertEquals(g1, g2);
    }
}
