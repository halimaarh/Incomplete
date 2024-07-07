//package com.example.chatapp;
//
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//public class ClientController {
//
//    @FXML
//    private VBox messageContainer;
//    @FXML
//    private TextField txtName;
//    @FXML
//    private TextField txtInput;
//
//    private DataOutputStream output;
//
//    public void setOutput(DataOutputStream output) {
//        this.output = output;
//    }
//
//    public void startReading(Socket socket) {
//        new Thread(new TaskRead(socket, this)).start();
//    }
//
//    @FXML
//    private void sendMessage() {
//        try {
//            String username = txtName.getText().trim();
//            String message = txtInput.getText().trim();
//            if (username.isEmpty()) {
//                username = "Unknown";
//            }
//            if (message.isEmpty()) {
//                return;
//            }
//            String formattedMessage = "[" + username + "]: " + message;
//            output.writeUTF(formattedMessage);
//            output.flush();
//            appendText(formattedMessage, true);
//            txtInput.clear();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void appendText(String message, boolean isSentByClient) {
//        Platform.runLater(() -> {
//            Text messageText = new Text(message + "\n");
//            messageText.setFont(Font.font("Arial", 14));
//            messageText.setFill(isSentByClient ? Color.BLUE : Color.GREEN);
//            TextFlow textFlow = new TextFlow(messageText);
//            textFlow.setStyle("-fx-background-color: " + (isSentByClient ? "#e1f5fe" : "#dcedc8") + "; -fx-padding: 10; -fx-background-radius: 5;");
//            messageContainer.getChildren().add(textFlow);
//        });
//    }
//}
//
//class TaskRead implements Runnable {
//    private final Socket socket;
//    private final ClientController clientController;
//
//    public TaskRead(Socket socket, ClientController clientController) {
//        this.socket = socket;
//        this.clientController = clientController;
//    }
//
//    @Override
//    public void run() {
//        try {
//            var input = new DataInputStream(socket.getInputStream());
//            while (true) {
//                String message = input.readUTF();
//                clientController.appendText(message, false);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//}

package com.example.chatapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtInput;

    private DataOutputStream output;
    private Socket socket;

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    public void startReading(Socket socket) {
        this.socket = socket;
        new Thread(new TaskRead(socket, this)).start();
    }

    @FXML
    private void sendMessage() {
        try {
            String username = txtName.getText().trim();
            String message = txtInput.getText().trim();
            if (username.isEmpty()) {
                username = "Unknown";
            }
            if (message.isEmpty()) {
                return;
            }
            String formattedMessage = "[" + username + "]: " + message;
            output.writeUTF(formattedMessage);
            output.flush();
            appendText(formattedMessage, true);
            txtInput.clear();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void appendText(String message, boolean isSentByClient) {
        Platform.runLater(() -> {
            Text messageText = new Text(message + "\n");
            messageText.setFill(isSentByClient ? Color.BLUE : Color.GREEN);
            TextFlow textFlow = new TextFlow(messageText);
            textFlow.setStyle("-fx-background-color: " + (isSentByClient ? "#2980b9" : "#27ae60") + "; -fx-padding: 10; -fx-background-radius: 5;");
            messageContainer.getChildren().add(textFlow);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket("localhost", 1234);
            setOutput(new DataOutputStream(socket.getOutputStream()));
            startReading(socket);
            appendText("Connected to server.\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLabel(String msgFromServer, VBox vBox) {
        Platform.runLater(() -> {
            Text messageText = new Text(msgFromServer + "\n");
            messageText.setFill(Color.GREEN);
            TextFlow textFlow = new TextFlow(messageText);
            textFlow.setStyle("-fx-background-color: #27ae60; -fx-padding: 10; -fx-background-radius: 5;");
            vBox.getChildren().add(textFlow);
        });
    }
}

class TaskRead implements Runnable {
    private final Socket socket;
    private final ClientController clientController;

    public TaskRead(Socket socket, ClientController clientController) {
        this.socket = socket;
        this.clientController = clientController;
    }

    @Override
    public void run() {
        try {
            var input = new DataInputStream(socket.getInputStream());
            while (true) {
                String message = input.readUTF();
                clientController.appendText(message, false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
