package com.example.chatapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController {

    @FXML
    private VBox messageContainer;

    private final List<ClientHandler> clients = new ArrayList<>();

    public void appendText(String text, String sender) {
        Platform.runLater(() -> {
            Text messageText = new Text(sender + ": " + text);
            messageText.setStyle("-fx-fill: white; -fx-font-size: 14px;");
            messageContainer.getChildren().add(messageText);
        });
    }

    public void addClientConnection(Socket socket) {
        try {
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        } catch (IOException ex) {
            appendText("Failed to add client: " + ex.getMessage(), "Server");
        }
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
        appendText(message, "Client " + clients.indexOf(sender));
    }
}

class ClientHandler implements Runnable {

    private final Socket socket;
    private final ServerController serverController;
    private final DataOutputStream output;

    public ClientHandler(Socket socket, ServerController serverController) throws IOException {
        this.socket = socket;
        this.serverController = serverController;
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            var input = new DataInputStream(socket.getInputStream());
            while (true) {
                String message = input.readUTF();
                serverController.broadcast(message, this);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
