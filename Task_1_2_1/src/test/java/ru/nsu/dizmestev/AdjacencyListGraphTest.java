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

    private StringVertex v(String name) {
        return new StringVertex(name);
    }

    @Test
    void testAddAndRemoveVertex() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        assertEquals(List.of(), g.getNeighbors(v("A")));
        g.removeVertex(v("B"));
        assertThrows(GraphException.class, () -> g.getNeighbors(v("B")));
    }

    @Test
    void testAddDuplicateVertex() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        assertThrows(GraphException.class, () -> g.addVertex(v("A")));
    }

    @Test
    void testRemoveNonExistentVertex() {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        assertThrows(GraphException.class, () -> g.removeVertex(v("A")));
    }

    @Test
    void testAddAndRemoveEdge() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        assertEquals(List.of(v("B")), g.getNeighbors(v("A")));
        g.removeEdge(v("A"), v("B"));
        assertEquals(List.of(), g.getNeighbors(v("A")));
    }

    @Test
    void testAddDuplicateEdge() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        assertThrows(GraphException.class, () -> g.addEdge(v("A"), v("B")));
    }

    @Test
    void testRemoveNonExistentEdge() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        assertThrows(GraphException.class, () -> g.removeEdge(v("A"), v("B")));
    }

    @Test
    void testAddEdgeWithNonExistentVertex() throws GraphException {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        assertThrows(GraphException.class, () -> g.addEdge(v("A"), v("B")));
    }

    @Test
    void testGetNeighborsNonExistentVertex() {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        assertThrows(GraphException.class, () -> g.getNeighbors(v("A")));
    }

    @Test
    void testTopologicalSort() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("C"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(List.of(v("A"), v("B"), v("C")), sorted);
    }

    @Test
    void testTopologicalSortSingleVertex() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        List<StringVertex> sorted = g.topologicalSort();
        assertEquals(List.of(v("A")), sorted);
    }

    @Test
    void testTopologicalSortMultipleRoots() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
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
    void testCycleDetection() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testCycleDetectionThreeVertices() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("C"));
        g.addEdge(v("C"), v("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testEquals() throws Exception {
        AdjacencyListGraph<StringVertex> g1 = new AdjacencyListGraph<>();
        AdjacencyListGraph<StringVertex> g2 = new AdjacencyListGraph<>();
        g1.addVertex(v("A"));
        g2.addVertex(v("A"));
        assertEquals(g1, g2);
    }

    @Test
    void testNotEquals() throws Exception {
        AdjacencyListGraph<StringVertex> g1 = new AdjacencyListGraph<>();
        AdjacencyListGraph<StringVertex> g2 = new AdjacencyListGraph<>();
        g1.addVertex(v("A"));
        g2.addVertex(v("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testNotEqualsDifferentEdges() throws Exception {
        AdjacencyListGraph<StringVertex> g1 = new AdjacencyListGraph<>();
        AdjacencyListGraph<StringVertex> g2 = new AdjacencyListGraph<>();
        g1.addVertex(v("A"));
        g1.addVertex(v("B"));
        g2.addVertex(v("A"));
        g2.addVertex(v("B"));
        g1.addEdge(v("A"), v("B"));
        assertNotEquals(g1, g2);
    }

    @Test
    void testToString() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addEdge(v("A"), v("B"));
        String result = g.toString();
        assertTrue(result.contains("A") && result.contains("B"));
    }

    @Test
    void testLoadFromFile(@TempDir Path tempDir) throws Exception {
        String content = "A\nB\nC\nA B\nB C";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, content);

        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
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

        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        assertThrows(GraphException.class, () -> g.loadFromFile(file.toString()));
    }

    @Test
    void testLoadFromFileNonExistent() {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        assertThrows(GraphException.class, () -> g.loadFromFile("nonexistent.txt"));
    }

    @Test
    void testSelfLoopDetection() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addEdge(v("A"), v("A"));
        assertThrows(GraphException.class, g::topologicalSort);
    }

    @Test
    void testGetVertices() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
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
    void testEmptyGraph() {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        assertTrue(g.getVertices().isEmpty());
    }

    @Test
    void testRemoveVertexUpdatesAllEdges() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
        g.addVertex(v("A"));
        g.addVertex(v("B"));
        g.addVertex(v("C"));
        g.addEdge(v("A"), v("B"));
        g.addEdge(v("B"), v("C"));
        g.addEdge(v("A"), v("C"));

        g.removeVertex(v("B"));

        assertEquals(List.of(v("C")), g.getNeighbors(v("A")));
        assertEquals(List.of(), g.getNeighbors(v("C")));
    }

    @Test
    void testComplexDependencyChain() throws Exception {
        AdjacencyListGraph<StringVertex> g = new AdjacencyListGraph<>();
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
}