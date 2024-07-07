package com.example.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server.fxml"));
            VBox root = loader.load();

            // Set application icon
            Image image = new Image(Client.class.getResourceAsStream("A.png"));
            primaryStage.getIcons().add(image);

            // Create a scene and display
            Scene scene = new Scene(root, 450, 500);
            primaryStage.setTitle("Server: Chat Box");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Get the controller instance from the loader
            ServerController controller = loader.getController();

            // Create a new thread to handle server socket
            new Thread(() -> {
                try {
                    // Create a server socket
                    ServerSocket serverSocket = new ServerSocket(1234);

                    // Append message to the TextArea in the UI (GUI Thread)
                    controller.appendText("New server started at " + new Date() + '\n', "Server");

                    // Continuous loop to accept connections
                    while (true) {
                        // Listen for a connection request
                        Socket socket = serverSocket.accept();
                        controller.addClientConnection(socket);
                    }
                } catch (IOException ex) {
                    controller.appendText(ex.toString() + '\n', "Server");
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
