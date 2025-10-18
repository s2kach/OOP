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
 * Тест для матрицы инцидентности.
 */
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
    void testAddDuplicateVertex() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        assertThrows(GraphException.class, () -> g.addVertex("A"));
    }

    @Test
    void testRemoveNonExistentVertex() {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        assertThrows(GraphException.class, () -> g.removeVertex("A"));
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
    void testRemoveNonExistentEdge() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        assertThrows(GraphException.class, () -> g.removeEdge("A", "B"));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() throws GraphException {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        assertThrows(GraphException.class, () -> g.addEdge("A", "B"));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        assertThrows(GraphException.class, () -> g.getNeighbors("A"));
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
    void testTopologicalSortSingleVertex() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        List<String> sorted = g.topologicalSort();
        assertEquals(List.of("A"), sorted);
    }

    @Test
    void testTopologicalSortMultipleRoots() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
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
    void testEquals() throws Exception {
        IncidenceMatrixGraph g1 = new IncidenceMatrixGraph();
        IncidenceMatrixGraph g2 = new IncidenceMatrixGraph();
        g1.addVertex("X");
        g2.addVertex("X");
        assertEquals(g1, g2);
    }

    @Test
    void testNotEquals() throws Exception {
        IncidenceMatrixGraph g1 = new IncidenceMatrixGraph();
        IncidenceMatrixGraph g2 = new IncidenceMatrixGraph();
        g1.addVertex("A");
        g2.addVertex("B");
        assertNotEquals(g1, g2);
    }

    @Test
    void testNotEqualsDifferentEdges() throws Exception {
        IncidenceMatrixGraph g1 = new IncidenceMatrixGraph();
        IncidenceMatrixGraph g2 = new IncidenceMatrixGraph();
        g1.addVertex("A");
        g1.addVertex("B");
        g2.addVertex("A");
        g2.addVertex("B");
        g1.addEdge("A", "B");
        assertNotEquals(g1, g2);
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

    @Test
    void testCycleDetectionThreeVertices() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("C", "A");
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testToString() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A", "B");
        String result = g.toString();
        assertTrue(result.contains("A") && result.contains("B") && result.contains("Matrix"));
    }

    @Test
    void testLoadFromFile(@TempDir Path tempDir) throws Exception {
        String content = "A\nB\nC\nA B\nB C";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, content);

        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
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

        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        assertThrows(GraphException.class, () -> g.loadFromFile(file.toString()));
    }

    @Test
    void testLoadFromFileNonExistent() {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        assertThrows(GraphException.class, () -> g.loadFromFile("nonexistent.txt"));
    }

    @Test
    void testRemoveVertexUpdatesEdges() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "C");

        g.removeVertex("B");

        assertEquals(List.of("C"), g.getNeighbors("A"));
        assertEquals(List.of(), g.getNeighbors("C"));
    }

    @Test
    void testMultipleEdgesFromSameVertex() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addEdge("A", "B");
        g.addEdge("A", "C");

        List<String> neighbors = g.getNeighbors("A");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("B") && neighbors.contains("C"));
    }

    @Test
    void testComplexGraphStructure() throws Exception {
        IncidenceMatrixGraph g = new IncidenceMatrixGraph();
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "D");
        g.addEdge("C", "D");

        List<String> sorted = g.topologicalSort();
        assertEquals(4, sorted.size());
        assertTrue(sorted.indexOf("A") < sorted.indexOf("B"));
        assertTrue(sorted.indexOf("A") < sorted.indexOf("C"));
        assertTrue(sorted.indexOf("B") < sorted.indexOf("D"));
        assertTrue(sorted.indexOf("C") < sorted.indexOf("D"));
    }
}