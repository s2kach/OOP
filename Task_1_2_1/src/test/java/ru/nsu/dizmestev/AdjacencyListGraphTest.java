package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тест для смежного списка.
 */
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
    void testAddDuplicateVertex() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        assertThrows(GraphException.class, () -> g.addVertex("A"));
    }

    @Test
    void testRemoveNonExistentVertex() {
        AdjacencyListGraph g = new AdjacencyListGraph();
        assertThrows(GraphException.class, () -> g.removeVertex("A"));
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
    void testAddDuplicateEdge() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        assertThrows(GraphException.class, () -> g.addEdge("A", "B"));
    }

    @Test
    void testRemoveNonExistentEdge() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        assertThrows(GraphException.class, () -> g.removeEdge("A", "B"));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() throws GraphException {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        assertThrows(GraphException.class, () -> g.addEdge("A", "B"));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        AdjacencyListGraph g = new AdjacencyListGraph();
        assertThrows(GraphException.class, () -> g.getNeighbors("A"));
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
    void testTopologicalSortSingleVertex() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        List<String> sorted = g.topologicalSort();
        assertEquals(List.of("A"), sorted);
    }

    @Test
    void testTopologicalSortMultipleRoots() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "C");
        g.addEdge("B", "C");
        List<String> sorted = g.topologicalSort();
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf("C") > sorted.indexOf("A"));
        assertTrue(sorted.indexOf("C") > sorted.indexOf("B"));
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
    void testCycleDetectionThreeVertices() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("C", "A");
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

    @Test
    void testNotEquals() throws Exception {
        AdjacencyListGraph g1 = new AdjacencyListGraph();
        AdjacencyListGraph g2 = new AdjacencyListGraph();
        g1.addVertex("A");
        g2.addVertex("B");
        assertNotEquals(g1, g2);
    }

    @Test
    void testNotEqualsDifferentEdges() throws Exception {
        AdjacencyListGraph g1 = new AdjacencyListGraph();
        AdjacencyListGraph g2 = new AdjacencyListGraph();
        g1.addVertex("A");
        g1.addVertex("B");
        g2.addVertex("A");
        g2.addVertex("B");
        g1.addEdge("A", "B");
        assertNotEquals(g1, g2);
    }

    @Test
    void testToString() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        String result = g.toString();
        assertTrue(result.contains("A") && result.contains("B"));
    }

    @Test
    void testLoadFromFile(@TempDir Path tempDir) throws Exception {
        String content = "A\nB\nC\nA B\nB C";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, content);

        AdjacencyListGraph g = new AdjacencyListGraph();
        g.loadFromFile(file.toString());

        assertEquals(List.of("B"), g.getNeighbors("A"));
        assertEquals(List.of("C"), g.getNeighbors("B"));
        assertEquals(List.of(), g.getNeighbors("C"));
    }

    @Test
    void testLoadFromFileInvalidLine(@TempDir Path tempDir) throws Exception {
        String content = "A B C";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, content);

        AdjacencyListGraph g = new AdjacencyListGraph();
        assertThrows(GraphException.class, () -> g.loadFromFile(file.toString()));
    }

    @Test
    void testLoadFromFileNonExistent() {
        AdjacencyListGraph g = new AdjacencyListGraph();
        assertThrows(GraphException.class, () -> g.loadFromFile("nonexistent.txt"));
    }

    @Test
    void testSelfLoopDetection() throws Exception {
        AdjacencyListGraph g = new AdjacencyListGraph();
        g.addVertex("A");
        g.addEdge("A", "A");
        assertThrows(GraphException.class, g::topologicalSort);
    }
}