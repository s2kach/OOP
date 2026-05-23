package ru.nsu.dizmestev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Вычислительный узел, подключающийся к мастеру для выполнения задач.
 */
public class WorkerNode {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    /**
     * Запускает цикл подключения и вычислений воркера.
     *
     * @throws NetworkException Если нет связи с мастером.
     */
    public void start() throws NetworkException {
        System.out.println("Воркер запущен. Подключение к мастеру...");
        try (
                Socket socket = new Socket(HOST, PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                TaskRequest request = (TaskRequest) ois.readObject();

                if (request.getType() == TaskRequest.Type.SHUTDOWN) {
                    System.out.println("Получен сигнал завершения от мастера.");
                    break;
                }

                if (request.getType() == TaskRequest.Type.PING) {
                    continue;
                }

                if (request.getType() == TaskRequest.Type.TASK) {
                    boolean foundNonPrime = false;
                    for (int number : request.getChunk()) {
                        if (!PrimeChecker.isPrime(number)) {
                            foundNonPrime = true;
                            break;
                        }
                    }
                    oos.writeObject(new TaskResponse(foundNonPrime));
                    oos.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof java.io.EOFException) {
                System.out.println("Мастер завершил работу и закрыл соединение.");
            } else {
                throw new NetworkException("Связь с мастером потеряна.", e);
            }
        }
    }
}
