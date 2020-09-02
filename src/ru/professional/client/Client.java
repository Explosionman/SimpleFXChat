package ru.professional.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private static Socket socket;
    private DataInputStream in;
    private static DataOutputStream out;
    private static StringBuilder sb = new StringBuilder();

    private Boolean register;
    private Controller chatController;

    public Client(Controller chatController) {

        this.chatController = chatController;
        this.register = false;
    }


    public static void sendMessage(String message) {
        if (message.isEmpty()) {
            return;
        }

        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (socket.isConnected()) {
                String msg = in.readUTF();
                String[] arr = msg.split("#");
                if (arr[0].equals("/MSG")) {
                    chatController.fldUsersText.appendText(arr[1] + "\n");
                }
                if (arr[0].equals("/INFO")) {
                    chatController.fldClients.setText("");
                    String[] names = arr[1].split(" ");
                    for (int i = 0; i < names.length; i++) {
                        chatController.fldClients.appendText(names[i] + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}