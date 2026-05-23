package ru.nsu.dizmestev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Главный узел, управляющий раздачей задач и агрегацией результатов.
 */
public class MasterNode {

    private static final int PORT = 8080;
    private static final int CHUNK_SIZE = 3;

    private final ConcurrentLinkedQueue<int[]> pendingTasks;
    private final AtomicBoolean globalFoundNonPrime;
    private final AtomicBoolean isFinished;
    private ServerSocket serverSocket;

    /**
     * Инициализирует мастер-узел исходным массивом.
     *
     * @param fullArray Полный массив для распределенной проверки.
     */
    public MasterNode(int[] fullArray) {
        this.pendingTasks = new ConcurrentLinkedQueue<>();
        this.globalFoundNonPrime = new AtomicBoolean(false);
        this.isFinished = new AtomicBoolean(false);
        splitIntoChunks(fullArray);
    }

    /**
     * Разбивает большой массив на мелкие куски для отправки узлам.
     *
     * @param array Исходный массив.
     */
    private void splitIntoChunks(int[] array) {
        for (int i = 0; i < array.length; i += CHUNK_SIZE) {
            int end = Math.min(array.length, i + CHUNK_SIZE);
            pendingTasks.add(Arrays.copyOfRange(array, i, end));
        }
    }

    /**
     * Запускает сервер и ожидает подключений от воркеров.
     *
     * @throws NetworkException Если не удалось запустить серверный сокет.
     */
    public void start() throws NetworkException {
        System.out.println("Мастер запущен. Ожидание воркеров...");
        try {
            this.serverSocket = new ServerSocket(PORT);
            while (!isFinished.get()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleWorker(clientSocket)).start();
            }
        } catch (IOException e) {
            if (!isFinished.get()) {
                throw new NetworkException("Ошибка работы серверного сокета на мастере.", e);
            }
        }
        System.out.println("Результат распределенного поиска: " + globalFoundNonPrime.get());
    }

    /**
     * Вспомогательный метод остановки для удобства и читаемости.
     */
    void stopServer() {
        isFinished.set(true);
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("close server socket: " + e.getMessage());
            }
        }
    }

    /**
     * Обрабатывает соединение с воркером.
     *
     * @param socket Сокет подключенного воркера.
     */
    private void handleWorker(Socket socket) {
        int[] currentChunk = null;
        try {
            socket.setSoTimeout(5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!isFinished.get()) {
                if (globalFoundNonPrime.get()) {
                    oos.writeObject(new TaskRequest(TaskRequest.Type.SHUTDOWN, null));
                    break;
                }

                currentChunk = pendingTasks.poll();
                if (currentChunk == null) {
                    oos.writeObject(new TaskRequest(TaskRequest.Type.SHUTDOWN, null));
                    stopServer();
                    break;
                }

                oos.writeObject(new TaskRequest(TaskRequest.Type.TASK, currentChunk));
                oos.flush();

                TaskResponse response = (TaskResponse) ois.readObject();

                if (response.isFoundNonPrime()) {
                    globalFoundNonPrime.set(true);
                    stopServer();
                }

                currentChunk = null;
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Воркер превысил время ожидания. Разрыв соединения.");
            returnTaskToQueue(currentChunk);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка взаимодействия с воркером: " + e.getMessage());
            returnTaskToQueue(currentChunk);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("close server socket: " + e.getMessage());
            }
        }
    }

    /**
     * Возвращает невыполненную задачу обратно в очередь для других узлов.
     *
     * @param chunk Массив чисел, который не был обработан.
     */
    private void returnTaskToQueue(int[] chunk) {
        if (chunk != null) {
            System.err.println("Задача возвращена в очередь.");
            pendingTasks.add(chunk);
        }
    }
}
