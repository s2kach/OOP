package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class AdjacencyMatrixGraphTest {

    @Test
    void testAddVertexAndEdge() throws Exception {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        assertEquals(List.of("B"), g.getNeighbors("A"));
    }

    @Test
    void testRemoveEdgeAndVertex() throws Exception {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        g.removeEdge("A", "B");
        assertEquals(List.of(), g.getNeighbors("A"));
        g.removeVertex("A");
        assertThrows(GraphException.class, () -> g.getNeighbors("A"));
    }

    @Test
    void testTopologicalSort() throws Exception {
        AdjacencyMatrixGraph g = new AdjacencyMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        List<String> sorted = g.topologicalSort();
        assertEquals(List.of("A", "B", "C"), sorted);
    }

    @Test
    void testEquals() throws Exception {
        AdjacencyMatrixGraph g1 = new AdjacencyMatrixGraph();
        AdjacencyMatrixGraph g2 = new AdjacencyMatrixGraph();
        g1.addVertex("X");
        g2.addVertex("X");
        assertEquals(g1, g2);
    }
}
