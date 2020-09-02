package ru.professional.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<Socket> clients = new ArrayList<>();
    static ArrayList<String> clientNames = new ArrayList<>();

    public static void main(String[] args) {

        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен");
            while (true) {
                socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("/MSG#" + "Введите свой никнэйм и нажмите enter");
                String nickname = in.readUTF();
                clients.add(socket);
                clientNames.add(nickname);
                System.out.println("Подключился клиент " + nickname);
                broadcastInfo(nickname);
                Thread thread = new Thread(() -> {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            broadcastMsg(nickname, str);
                            System.out.println("Клиент " + nickname + " прислал сообщение: " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void broadcastMsg(String name, String str) throws IOException {
        DataOutputStream out;
        for (Socket socket : clients) {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("/MSG#" + name + ": " + str);
        }
    }

    public static void broadcastInfo(String name) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clientNames.size(); i++) {
            sb.append(clientNames.get(i) + " ");
        }
        DataOutputStream out;
        for (Socket socket : clients) {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("/MSG#" + name + " зашёл в чат");
            out.writeUTF("/INFO#" + sb.toString());
            out.flush();
        }
    }
}
