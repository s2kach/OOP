package ru.nsu.dizmestev;

/**
 * Провайдер колоды карт для тестирования.
 */
public interface DeckProvider {
    /**
     * Получить колоду карт.
     *
     * @return колода карт
     */
    Deck getDeck();
}