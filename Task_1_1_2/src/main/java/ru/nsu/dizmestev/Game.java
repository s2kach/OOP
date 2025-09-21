package ru.nsu.dizmestev;

import java.util.Scanner;

/**
 * Основной класс игры.
 */
public class Game {
    private Deck deck;
    private final Player player;
    private final Dealer dealer;
    private int playerWins = 0;
    private int dealerWins = 0;
    private int roundNumber = 1;
    private final int numberOfDecks;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Создаём игрока и дилера.
     *
     * @param numberOfDecks записали кол-во колод
     */
    public Game(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        this.player = new Player();
        this.dealer = new Dealer();
    }

    /**
     * Начало игры.
     */
    public void start() {
        while (true) {
            startRound();
            System.out.println("Счет " + playerWins + ":" + dealerWins);

            boolean validInput = false;
            String input = "";
            System.out.print("Нажмите Enter для продолжения или 'q' для выхода: ");
            while (!validInput) {
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("q") || input.isEmpty()) {
                    validInput = true;
                } else {
                    System.out.print("Неверный ввод. Пожалуйста, просто нажмите Enter "
                            + "или введите 'q': ");
                }
            }

            if (input.equalsIgnoreCase("q")) {
                break;
            }
            roundNumber++;
        }
    }

    /**
     * Раунд игры.
     */
    private void startRound() {
        deck = new Deck(this.numberOfDecks);
        System.out.println("\n===============================================================");
        System.out.println("Раунд " + roundNumber);
        System.out.println("===============================================================");
        clearHands();
        dealInitialCards();

        System.out.println("Дилер раздал карты");
        printHands(false);

        // Проверка на блэкджек
        if (player.getHand().hasBlackjack()) {
            System.out.println("----------------------------------");
            System.out.println("У вас блэкджек! Вы выиграли раунд!");
            System.out.println("----------------------------------");
            playerWins++;
            return;
        }

        // Ход игрока
        playerTurn();

        // Если игрок не перебрал - ход дилера
        if (player.getHand().calculateScore() <= 21) {
            dealerTurn();
        }

        determineWinner();
    }

    /**
     * Очистка карт перед началом следующего раунда.
     */
    private void clearHands() {
        player.clearHand();
        dealer.clearHand();
    }

    /**
     * Выдаём начальные карты игроку и дилеру.
     */
    private void dealInitialCards() {
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
    }

    /**
     * Ход игрока.
     */
    private void playerTurn() {
        System.out.println("---===---");
        System.out.println("Ваш ход");
        System.out.println("---===---");



        while (true) {
            boolean choice = player.shouldTakeCard();

            if (!choice) {
                break;
            }

            Card newCard = deck.drawCard();
            player.takeCard(newCard);
            System.out.println("Вы открыли карту " + newCard);
            printHands(false);


            if (player.getHand().calculateScore() > 21) {
                System.out.println("Перебор! Вы проиграли раунд.");
                break;
            }
        }
    }

    /**
     * Ход дилера.
     */
    private void dealerTurn() {
        System.out.println("----====----");
        System.out.println("Ход дилера");
        System.out.println("----====----");

        Card hiddenCard = dealer.getHand().getCards().get(1);
        System.out.println("Дилер открывает закрытую карту " + hiddenCard);

        while (dealer.shouldTakeCard()) {
            System.out.println("----------------------------------");
            Card newCard = deck.drawCard();
            dealer.takeCard(newCard);
            System.out.println("Дилер открывает карту " + newCard);
        }
        printHands(true);
    }

    /**
     * Определить победителя.
     */
    private void determineWinner() {
        int playerScore = player.getHand().calculateScore();
        int dealerScore = dealer.getHand().calculateScore();

        if (playerScore > 21) {
            dealerWins++;
            System.out.println("Дилер выиграл раунд!");
        } else if (dealerScore > 21) {
            playerWins++;
            System.out.println("Вы выиграли раунд!");
        } else if (playerScore > dealerScore) {
            playerWins++;
            System.out.println("Вы выиграли раунд!");
        } else if (dealerScore > playerScore) {
            dealerWins++;
            System.out.println("Дилер выиграл раунд!");
        } else {
            System.out.println("Ничья!");
        }
    }

    /**
     * Выводим текущие карты.
     *
     * @param showDealerCards показываем карту диллера или нет
     */
    private void printHands(boolean showDealerCards) {
        System.out.println("----------------------------------");
        System.out.print("Ваши карты: ");
        System.out.print(player.getHand().getCards());
        System.out.println(" > " + player.getHand().calculateScore());

        System.out.print("Карты дилера: ");
        if (showDealerCards) {
            System.out.print(dealer.getHand().getCards());
            System.out.println(" > " + dealer.getHand().calculateScore());
        } else {
            // Показываем только первую карту дилера
            System.out.print("[" + dealer.getHand().getCards().get(0) + ", <закрытая карта>]");
            System.out.println();
        }
        System.out.println("----------------------------------");
    }
}