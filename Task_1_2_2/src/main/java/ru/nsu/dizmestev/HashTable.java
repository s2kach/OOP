package ru.nsu.dizmestev;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Реализация параметризованной хеш-таблицы с обработкой коллизий.
 *
 * @param <K> Тип ключа.
 * @param <V> Тип значения.
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private List<Entry<K, V>>[] buckets;
    private int size;
    private int modCount;

    /**
     * Создает пустую хеш-таблицу.
     *
     * @throws HashTableException Если произошла ошибка при создании таблицы.
     */
    @SuppressWarnings("unchecked")
    public HashTable() throws HashTableException {
        try {
            buckets = new List[DEFAULT_CAPACITY];
        } catch (Exception e) {
            throw new HashTableException("Ошибка создания хеш-таблицы.", e);
        }
    }

    /**
     * Добавляет новую пару ключ-значение.
     *
     * @param key Ключ.
     * @param value Значение.
     * @throws HashTableException Если ключ или значение равны null или ключ уже существует.
     */
    public void put(K key, V value) throws HashTableException {
        if (key == null || value == null) {
            throw new InvalidKeyOrValueException("Ключ и значение не могут быть null.");
        }
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                throw new DuplicateKeyException("Ключ уже существует: " + key);
            }
        }

        buckets[index].add(new Entry<>(key, value));
        size++;
        modCount++;

        if ((float) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    /**
     * Обновляет значение по ключу.
     *
     * @param key Ключ.
     * @param value Новое значение.
     * @throws HashTableException Если ключ не найден.
     */
    public void update(K key, V value) throws HashTableException {
        V old = get(key);
        remove(key);
        put(key, value);
    }

    /**
     * Возвращает значение по ключу.
     *
     * @param key Ключ.
     * @return Значение.
     * @throws NotFoundException Если ключ не найден.
     */
    public V get(K key) throws NotFoundException {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        throw new NotFoundException("Ключ не найден: " + key);
    }

    /**
     * Удаляет элемент по ключу.
     *
     * @param key Ключ.
     * @throws NotFoundException Если ключ не найден.
     */
    public void remove(K key) throws NotFoundException {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets[index];
        if (bucket != null) {
            Iterator<Entry<K, V>> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove();
                    size--;
                    modCount++;
                    return;
                }
            }
        }
        throw new NotFoundException("Ключ не найден: " + key);
    }

    /**
     * Проверяет наличие значения по ключу.
     *
     * @param key Ключ.
     * @return true, если значение найдено, иначе false.
     */
    public boolean containsKey(K key) {
        try {
            get(key);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    /**
     * Возвращает размер таблицы.
     *
     * @return Количество элементов.
     */
    public int size() {
        return size;
    }

    /**
     * Возвращает строковое представление таблицы.
     *
     * @return Строка с элементами таблицы.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> entry : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.key).append("=").append(entry.value);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Сравнивает таблицу с другой.
     *
     * @param o Объект для сравнения.
     * @return true, если таблицы равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashTable<?, ?> other)) {
            return false;
        }
        if (size != other.size) {
            return false;
        }

        for (Entry<K, V> entry : this) {
            boolean found = false;
            for (Entry<?, ?> obj : other) {
                Entry<?, ?> otherEntry = obj;
                if (Objects.equals(entry.key, otherEntry.key)
                        && Objects.equals(entry.value, otherEntry.value)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }


    /**
     * Возвращает итератор с контролем модификаций.
     *
     * @return Итератор по парам (ключ, значение).
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Возвращает индекс бакета для ключа.
     *
     * @param key Ключ.
     * @return Индекс бакета.
     */
    private int getIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    /**
     * Изменяет размер таблицы при превышении порога загрузки.
     *
     * @throws HashTableException Если произошла ошибка при перераспределении.
     */
    @SuppressWarnings("unchecked")
    private void resize() throws HashTableException {
        try {
            List<Entry<K, V>>[] oldBuckets = buckets;
            buckets = new List[oldBuckets.length * 2];
            size = 0;
            for (List<Entry<K, V>> bucket : oldBuckets) {
                if (bucket != null) {
                    for (Entry<K, V> entry : bucket) {
                        put(entry.key, entry.value);
                    }
                }
            }
        } catch (Exception e) {
            throw new HashTableException("Ошибка при изменении размера таблицы.", e);
        }
    }

    /**
     * Класс записи пары ключ-значение.
     *
     * @param <K> Тип ключа.
     * @param <V> Тип значения.
     */
    public static class Entry<K, V> {
        public final K key;
        public final V value;

        /**
         * Создает новую пару ключ-значение.
         *
         * @param key Ключ.
         * @param value Значение.
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Сравнивает записи по ключу и значению.
         *
         * @param o Объект для сравнения.
         * @return true, если записи равны.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Entry<?, ?> entry)) {
                return false;
            }
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }

        /**
         * Возвращает хеш-код записи.
         *
         * @return Хеш-код.
         */
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    /**
     * Итератор по хеш-таблице с контролем модификаций.
     */
    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private int bucketIndex = 0;
        private Iterator<Entry<K, V>> currentIterator = null;
        private final int expectedModCount = modCount;

        /**
         * Проверяет наличие следующего элемента.
         *
         * @return true, если элемент есть, иначе false.
         */
        @Override
        public boolean hasNext() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException(
                        "Хеш-таблица была изменена во время итерации.");
            }
            while ((currentIterator == null || !currentIterator.hasNext())
                    && bucketIndex < buckets.length) {
                if (buckets[bucketIndex] != null) {
                    currentIterator = buckets[bucketIndex].iterator();
                }
                bucketIndex++;
            }
            return currentIterator != null && currentIterator.hasNext();
        }

        /**
         * Возвращает следующий элемент итерации.
         *
         * @return Пару ключ-значение.
         */
        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return currentIterator.next();
        }
    }
}
