package ru.nsu.dizmestev;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Вычислительный узел, динамически находящий мастера в подсети.
 */
public class WorkerNode {

    private final int udpPort;

    /**
     * Конструктор принимающий порт приложения в котором искать мастера в сети.
     *
     * @param udpPort Собственно сам порт.
     */
    public WorkerNode(int udpPort) {
        this.udpPort = udpPort;
    }

    /**
     * Находит мастер-узел по UDP Broadcast и запускает рабочий цикл TCP.
     *
     * @throws NetworkException Если нет связи с мастером.
     */
    public void start() throws NetworkException {
        System.out.println("Воркер запущен. Поиск мастера в подсети...");

        String masterHost;
        int masterPort;

        try (DatagramSocket udpSocket = new DatagramSocket()) {
            udpSocket.setSoTimeout(5000);
            byte[] requestData = "DISCOVER_MASTER".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(
                    requestData, requestData.length,
                    InetAddress.getByName("255.255.255.255"), udpPort
            );
            udpSocket.send(sendPacket);

            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0,
                    receivePacket.getLength()).trim();
            if (response.startsWith("I_AM_MASTER:")) {
                masterHost = receivePacket.getAddress().getHostAddress();
                masterPort = Integer.parseInt(response.split(":")[1]);
                System.out.println("Мастер успешно найден по адресу: "
                        + masterHost + ":" + masterPort);
            } else {
                throw new NetworkException("Получен некорректный ответ идентификации от мастера.");
            }
        } catch (IOException e) {
            throw new NetworkException("Не удалось обнаружить мастер-узел в локальной сети.", e);
        }

        try (
                Socket socket = new Socket(masterHost, masterPort);
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
