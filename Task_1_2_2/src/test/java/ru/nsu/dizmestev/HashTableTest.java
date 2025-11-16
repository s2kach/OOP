package ru.nsu.dizmestev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование реализации хеш-таблицы.
 */
class HashTableTest {

    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() throws HashTableException {
        table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
    }

    @Test
    void testPutAndGet() throws HashTableException {
        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
        assertEquals(3, table.get("three"));
    }

    @Test
    void testPutDuplicateKey() {
        assertThrows(DuplicateKeyException.class, () -> table.put("one", 10));
    }

    @Test
    void testPutNullKeyOrValue() {
        assertThrows(InvalidKeyOrValueException.class, () -> table.put(null, 5));
        assertThrows(InvalidKeyOrValueException.class, () -> table.put("key", null));
    }

    @Test
    void testGetNonExistentKey() {
        assertThrows(NotFoundException.class, () -> table.get("unknown"));
    }

    @Test
    void testRemove() throws HashTableException {
        table.remove("two");
        assertThrows(NotFoundException.class, () -> table.get("two"));
        assertEquals(2, table.size());
    }

    @Test
    void testRemoveNonExistentKey() {
        assertThrows(NotFoundException.class, () -> table.remove("unknown"));
    }

    @Test
    void testUpdate() throws HashTableException {
        table.update("one", 100);
        assertEquals(100, table.get("one"));
    }

    @Test
    void testUpdateNonExistentKey() {
        assertThrows(NotFoundException.class, () -> table.update("unknown", 100));
    }

    @Test
    void testContainsKey() {
        assertTrue(table.containsKey("one"));
        assertFalse(table.containsKey("unknown"));
    }

    @Test
    void testSize() throws HashTableException {
        assertEquals(3, table.size());
        table.remove("one");
        assertEquals(2, table.size());
    }

    @Test
    void testIterator() {
        Iterator<HashTable.Entry<String, Integer>> iterator = table.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testIteratorNoSuchElement() {
        Iterator<HashTable.Entry<String, Integer>> iterator = table.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorConcurrentModification() {
        Iterator<HashTable.Entry<String, Integer>> iterator = table.iterator();
        assertThrows(ConcurrentModificationException.class, () -> {
            iterator.hasNext();
            table.put("four", 4);
            iterator.next();
        });
    }

    @Test
    void testEquals() throws HashTableException {
        HashTable<String, Integer> other = new HashTable<>();
        other.put("one", 1);
        other.put("two", 2);
        other.put("three", 3);

        assertTrue(table.equals(other));

        other.put("four", 4);
        assertFalse(table.equals(other));
    }

    @Test
    void testToString() {
        String result = table.toString();
        assertTrue(result.contains("one=1"));
        assertTrue(result.contains("two=2"));
        assertTrue(result.contains("three=3"));
    }

    @Test
    void testResize() throws HashTableException {
        for (int i = 0; i < 20; i++) {
            table.put("key" + i, i);
        }
        assertEquals(23, table.size());
    }
}