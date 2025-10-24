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
 * Тест для смежной матрицы.
 */
public class AdjacencyMatrixGraphTest {

    private StringVertex createVertex(String name) {
        return new StringVertex(name);
    }

    @Test
    void testAddVertexAndEdge() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        assertEquals(List.of(createVertex("B")), g.getNeighbors(createVertex("A")));
    }

    @Test
    void testAddDuplicateVertex() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        assertThrows(GraphException.class, () -> g.addVertex(createVertex("A")));
    }

    @Test
    void testRemoveNonExistentVertex() {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.removeVertex(createVertex("A")));
    }

    @Test
    void testRemoveEdgeAndVertex() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.removeEdge(createVertex("A"), createVertex("B"));
        assertEquals(List.of(), g.getNeighbors(createVertex("A")));
        g.removeVertex(createVertex("A"));
        assertThrows(GraphException.class, () -> g.getNeighbors(createVertex("A")));
    }

    @Test
    void testAddDuplicateEdge() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        assertThrows(GraphException.class, () -> g.addEdge(createVertex("A"), createVertex("B")));
    }

    @Test
    void testRemoveNonExistentEdge() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        assertThrows(GraphException.class,
                () -> g.removeEdge(createVertex("A"), createVertex("B")));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() throws GraphException {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        assertThrows(GraphException.class, () -> g.addEdge(createVertex("A"), createVertex("B")));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.getNeighbors(createVertex("A")));
    }

    @Test
    void testTopologicalSort() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("B"), createVertex("C"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(List.of(createVertex("A"), createVertex("B"), createVertex("C")), sorted);
    }

    @Test
    void testTopologicalSortSingleVertex() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(List.of(createVertex("A")), sorted);
    }

    @Test
    void testTopologicalSortMultipleRoots() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
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
    void testCycleDetection() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("B"), createVertex("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testCycleDetectionThreeVertices() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("B"), createVertex("C"));
        g.addEdge(createVertex("C"), createVertex("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testEquals() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g1 = new AdjacencyMatrixGraph<>();
        AdjacencyMatrixGraph<StringVertex> g2 = new AdjacencyMatrixGraph<>();
        g1.addVertex(createVertex("X"));
        g2.addVertex(createVertex("X"));
        assertEquals(g1, g2);
    }

    @Test
    void testNotEquals() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g1 = new AdjacencyMatrixGraph<>();
        AdjacencyMatrixGraph<StringVertex> g2 = new AdjacencyMatrixGraph<>();
        g1.addVertex(createVertex("A"));
        g2.addVertex(createVertex("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testNotEqualsDifferentEdges() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g1 = new AdjacencyMatrixGraph<>();
        AdjacencyMatrixGraph<StringVertex> g2 = new AdjacencyMatrixGraph<>();
        g1.addVertex(createVertex("A"));
        g1.addVertex(createVertex("B"));
        g2.addVertex(createVertex("A"));
        g2.addVertex(createVertex("B"));
        g1.addEdge(createVertex("A"), createVertex("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testToString() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
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

        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
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

        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.loadFromFile(file.toString()));
    }

    @Test
    void testLoadFromFileNonExistent() {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        assertThrows(GraphException.class, () -> g.loadFromFile("nonexistent.txt"));
    }

    @Test
    void testRemoveVertexUpdatesEdges() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
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
    void testSelfLoopDetection() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addEdge(createVertex("A"), createVertex("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testMultipleEdgesFromSameVertex() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
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
    void testGetVertices() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
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
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        assertTrue(g.getVertices().isEmpty());
    }

    @Test
    void testComplexDependencies() throws Exception {
        AdjacencyMatrixGraph<StringVertex> g = new AdjacencyMatrixGraph<>();
        g.addVertex(createVertex("A"));
        g.addVertex(createVertex("B"));
        g.addVertex(createVertex("C"));
        g.addVertex(createVertex("D"));
        g.addVertex(createVertex("E"));

        g.addEdge(createVertex("A"), createVertex("B"));
        g.addEdge(createVertex("A"), createVertex("C"));
        g.addEdge(createVertex("B"), createVertex("D"));
        g.addEdge(createVertex("C"), createVertex("D"));
        g.addEdge(createVertex("D"), createVertex("E"));

        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(5, sorted.size());
        assertTrue(sorted.indexOf(createVertex("A")) < sorted.indexOf(createVertex("B")));
        assertTrue(sorted.indexOf(createVertex("A")) < sorted.indexOf(createVertex("C")));
        assertTrue(sorted.indexOf(createVertex("B")) < sorted.indexOf(createVertex("D")));
        assertTrue(sorted.indexOf(createVertex("C")) < sorted.indexOf(createVertex("D")));
        assertTrue(sorted.indexOf(createVertex("D")) < sorted.indexOf(createVertex("E")));
    }
}