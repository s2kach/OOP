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

    private StringVertex createVertex(String name) {
        return new StringVertex(name);
    }

    @Test
    void testAddAndGetNeighbors() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        assertEquals(List.of(createVertex("B")), g.getNeighbors(createVertex("A")));
    }

    @Test
    void testAddDuplicateVertex() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        assertThrows(GraphException.class, () -> g.addVertex(createVertex("A")));
    }

    @Test
    void testRemoveNonExistentVertex() {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.removeVertex(createVertex("A")));
    }

    @Test
    void testRemoveEdge() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.removeEdge(createVertex("A"), createVertex("B"));
        assertEquals(List.of(), g.getNeighbors(createVertex("A")));
    }

    @Test
    void testRemoveNonExistentEdge() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        assertThrows(GraphException.class,
                () -> g.removeEdge(createVertex("A"), createVertex("B")));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() throws GraphException {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        assertThrows(GraphException.class, () -> g.addEdge(createVertex("A"), createVertex("B")));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.getNeighbors(createVertex("A")));
    }

    @Test
    void testTopologicalSort() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("B"), createVertex("C"));
        assertEquals(List.of(createVertex("A"),
                createVertex("B"), createVertex("C")), g.topologicalSort());
    }

    @Test
    void testTopologicalSortSingleVertex() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(List.of(createVertex("A")), sorted);
    }

    @Test
    void testTopologicalSortMultipleRoots() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("C"));
        g.addEdge(createVertex("B"), createVertex("C"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(createVertex("C")) > sorted.indexOf(createVertex("A")));
        assertTrue(sorted.indexOf(createVertex("C")) > sorted.indexOf(createVertex("B")));
    }

    @Test
    void testEquals() throws Exception {
        IncidenceMatrixGraph<StringVertex> g1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<StringVertex> g2 = new IncidenceMatrixGraph<>();
        g1.addVertex(createVertex("X"));
        g2.addVertex(createVertex("X"));
        assertEquals(g1, g2);
    }

    @Test
    void testNotEquals() throws Exception {
        IncidenceMatrixGraph<StringVertex> g1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<StringVertex> g2 = new IncidenceMatrixGraph<>();
        g1.addVertex(createVertex("A"));
        g2.addVertex(createVertex("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testNotEqualsDifferentEdges() throws Exception {
        IncidenceMatrixGraph<StringVertex> g1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<StringVertex> g2 = new IncidenceMatrixGraph<>();
        g1.addVertex(createVertex("A"));
        g1.addVertex(createVertex("B"));
        g2.addVertex(createVertex("A"));
        g2.addVertex(createVertex("B"));
        g1.addEdge(createVertex("A"), createVertex("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testCycleDetection() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("B"), createVertex("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testCycleDetectionThreeVertices() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("B"), createVertex("C"));
        g.addEdge(createVertex("C"), createVertex("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testToString() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        String result = g.toString();
        assertTrue(result.contains("A") && result.contains("B") && result.contains("Matrix"));
    }

    @Test
    void testLoadFromFile(@TempDir Path tempDir) throws Exception {
        String content = "A\nB\nC\nA B\nB C";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, content);

        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.loadFromFile(file.toString());

        assertEquals(List.of(createVertex("B")), g.getNeighbors(createVertex("A")));
        assertEquals(List.of(createVertex("C")), g.getNeighbors(createVertex("B")));
        assertEquals(List.of(), g.getNeighbors(createVertex("C")));
    }

    @Test
    void testLoadFromFileInvalidLine(@TempDir Path tempDir) throws Exception {
        String content = "A B C";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, content);

        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.loadFromFile(file.toString()));
    }

    @Test
    void testLoadFromFileNonExistent() {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.loadFromFile("nonexistent.txt"));
    }

    @Test
    void testRemoveVertexUpdatesEdges() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("C"));
        g.addEdge(createVertex("B"), createVertex("C"));

        g.removeVertex(createVertex("B"));

        assertEquals(List.of(createVertex("C")), g.getNeighbors(createVertex("A")));
        assertEquals(List.of(), g.getNeighbors(createVertex("C")));
    }

    @Test
    void testMultipleEdgesFromSameVertex() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("C"));

        List<StringVertex> neighbors = g.getNeighbors(createVertex("A"));
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(createVertex("B")) && neighbors.contains(createVertex("C")));
    }

    @Test
    void testComplexGraphStructure() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addVertex(createVertex("D"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("C"));
        g.addEdge(createVertex("B"), createVertex("D"));
        g.addEdge(createVertex("C"), createVertex("D"));

        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(4, sorted.size());
        assertTrue(sorted.indexOf(createVertex("A")) < sorted.indexOf(createVertex("B")));
        assertTrue(sorted.indexOf(createVertex("A")) < sorted.indexOf(createVertex("C")));
        assertTrue(sorted.indexOf(createVertex("B")) < sorted.indexOf(createVertex("D")));
        assertTrue(sorted.indexOf(createVertex("C")) < sorted.indexOf(createVertex("D")));
    }

    @Test
    void testGetVertices() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));

        List<StringVertex> vertices = g.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains(createVertex("A")));
        assertTrue(vertices.contains(createVertex("B")));
        assertTrue(vertices.contains(createVertex("C")));
    }

    @Test
    void testEmptyGraph() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertTrue(g.getVertices().isEmpty());
    }

    @Test
    void testSelfLoopAddition() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(createVertex("A"));

        assertThrows(GraphException.class, () -> g.addEdge(createVertex("A"), createVertex("A")));
    }
}