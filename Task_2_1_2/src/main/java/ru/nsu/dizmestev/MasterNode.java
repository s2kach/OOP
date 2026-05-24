package ru.nsu.dizmestev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

    private final int udpPort;
    private final int port;
    private final int chunkSize;
    private final ConcurrentLinkedQueue<int[]> pendingTasks;
    private final AtomicBoolean globalFoundNonPrime;
    private final AtomicBoolean isFinished;
    private ServerSocket serverSocket;
    private DatagramSocket udpSocket;

    /**
     * Инициализирует мастер-узел исходным массивом.
     *
     * @param fullArray Полный массив для распределенной проверки.
     */
    public MasterNode(int[] fullArray, int port, int udpPort, int chunkSize) {
        this.port = port;
        this.udpPort = udpPort;
        this.chunkSize = chunkSize;
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
        for (int i = 0; i < array.length; i += chunkSize) {
            int end = Math.min(array.length, i + chunkSize);
            pendingTasks.add(Arrays.copyOfRange(array, i, end));
        }
    }

    /**
     * Запускает сервер, UDP-дискавери и ожидает подключений в пуле потоков.
     *
     * @throws NetworkException Если не удалось запустить серверный сокет.
     */
    public void start() throws NetworkException {
        Thread discoveryThread = new Thread(this::runDiscoveryServer);
        discoveryThread.setDaemon(true);
        discoveryThread.start();

        System.out.println("Мастер запущен на TCP порту " + port + ". Ожидание воркеров...");
        try {
            this.serverSocket = new ServerSocket(port);
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
     * Фоновый UDP-сервер, отвечающий воркерам на широковещательные запросы.
     */
    private void runDiscoveryServer() {
        try {
            this.udpSocket = new DatagramSocket(null);
            this.udpSocket.setReuseAddress(true);
            this.udpSocket.bind(new java.net.InetSocketAddress(udpPort));

            byte[] buffer = new byte[1024];
            while (!isFinished.get()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength()).trim();
                if ("DISCOVER_MASTER".equals(message)) {
                    byte[] responseData = ("I_AM_MASTER:" + port).getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData, responseData.length, packet.getAddress(), packet.getPort()
                    );
                    udpSocket.send(responsePacket);
                }
            }
        } catch (IOException e) {
            System.out.println("UDP Discovery сервер остановлен: " + e.getMessage());
        }
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
