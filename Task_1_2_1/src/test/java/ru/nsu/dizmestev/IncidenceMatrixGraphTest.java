package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class IncidenceMatrixGraphTest {

    @Test
    void testAddAndGetNeighbors() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        assertEquals(List.of("B"), g.getNeighbors("A"));
    }

    @Test
    void testRemoveEdge() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        g.removeEdge("A", "B");
        assertEquals(List.of(), g.getNeighbors("A"));
    }

    @Test
    void testTopologicalSort() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        assertEquals(List.of("A", "B", "C"), g.topologicalSort());
    }

    @Test
    void testEquals() throws Exception {
        IncidenceMatrixGraph g1 = new IncidenceMatrixGraph();
        IncidenceMatrixGraph g2 = new IncidenceMatrixGraph();
        g1.addVertex("X");
        g2.addVertex("X");
        assertEquals(g1, g2);
    }

    @Test
    void testCycleDetection() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        g.addEdge("B", "A");
        assertThrows(GraphException.class, g::topologicalSort);
    }
}
