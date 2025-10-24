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

    private StringVertex v(String name) {
        return new StringVertex(name);
    }

    @Test
    void testAddAndGetNeighbors() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        assertEquals(List.of(v("B")), g.getNeighbors(v("A")));
    }

    @Test
    void testAddDuplicateVertex() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        assertThrows(GraphException.class, () -> g.addVertex(v("A")));
    }

    @Test
    void testRemoveNonExistentVertex() {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.removeVertex(v("A")));
    }

    @Test
    void testRemoveEdge() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        g.removeEdge(v("A"), v("B"));
        assertEquals(List.of(), g.getNeighbors(v("A")));
    }

    @Test
    void testRemoveNonExistentEdge() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        assertThrows(GraphException.class, () -> g.removeEdge(v("A"), v("B")));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() throws GraphException {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        assertThrows(GraphException.class, () -> g.addEdge(v("A"), v("B")));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.getNeighbors(v("A")));
    }

    @Test
    void testTopologicalSort() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("C"));
        assertEquals(List.of(v("A"), v("B"), v("C")), g.topologicalSort());
    }

    @Test
    void testTopologicalSortSingleVertex() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(List.of(v("A")), sorted);
    }

    @Test
    void testTopologicalSortMultipleRoots() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("C"));
        g.addEdge(v("B"), v("C"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(v("C")) > sorted.indexOf(v("A")));
        assertTrue(sorted.indexOf(v("C")) > sorted.indexOf(v("B")));
    }

    @Test
    void testEquals() throws Exception {
        IncidenceMatrixGraph<StringVertex> g1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<StringVertex> g2 = new IncidenceMatrixGraph<>();
        g1.addVertex(v("X"));
        g2.addVertex(v("X"));
        assertEquals(g1, g2);
    }

    @Test
    void testNotEquals() throws Exception {
        IncidenceMatrixGraph<StringVertex> g1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<StringVertex> g2 = new IncidenceMatrixGraph<>();
        g1.addVertex(v("A"));
        g2.addVertex(v("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testNotEqualsDifferentEdges() throws Exception {
        IncidenceMatrixGraph<StringVertex> g1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<StringVertex> g2 = new IncidenceMatrixGraph<>();
        g1.addVertex(v("A"));
        g1.addVertex(v("B"));
        g2.addVertex(v("A"));
        g2.addVertex(v("B"));
        g1.addEdge(v("A"), v("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testCycleDetection() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testCycleDetectionThreeVertices() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("C"));
        g.addEdge(v("C"), v("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testToString() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
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

        assertEquals(List.of(v("B")), g.getNeighbors(v("A")));
        assertEquals(List.of(v("C")), g.getNeighbors(v("B")));
        assertEquals(List.of(), g.getNeighbors(v("C")));
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
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("A"), v("C"));
        g.addEdge(v("B"), v("C"));

        g.removeVertex(v("B"));

        assertEquals(List.of(v("C")), g.getNeighbors(v("A")));
        assertEquals(List.of(), g.getNeighbors(v("C")));
    }

    @Test
    void testMultipleEdgesFromSameVertex() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("A"), v("C"));

        List<StringVertex> neighbors = g.getNeighbors(v("A"));
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(v("B")) && neighbors.contains(v("C")));
    }

    @Test
    void testComplexGraphStructure() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addVertex(v("D"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("A"), v("C"));
        g.addEdge(v("B"), v("D"));
        g.addEdge(v("C"), v("D"));

        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(4, sorted.size());
        assertTrue(sorted.indexOf(v("A")) < sorted.indexOf(v("B")));
        assertTrue(sorted.indexOf(v("A")) < sorted.indexOf(v("C")));
        assertTrue(sorted.indexOf(v("B")) < sorted.indexOf(v("D")));
        assertTrue(sorted.indexOf(v("C")) < sorted.indexOf(v("D")));
    }

    @Test
    void testGetVertices() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));

        List<StringVertex> vertices = g.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains(v("A")));
        assertTrue(vertices.contains(v("B")));
        assertTrue(vertices.contains(v("C")));
    }

    @Test
    void testEmptyGraph() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        assertTrue(g.getVertices().isEmpty());
    }

    @Test
    void testSelfLoopAddition() throws Exception {
        IncidenceMatrixGraph<StringVertex> g = new IncidenceMatrixGraph<>();
        g.addVertex(v("A"));

        assertThrows(GraphException.class, () -> g.addEdge(v("A"), v("A")));
    }
}