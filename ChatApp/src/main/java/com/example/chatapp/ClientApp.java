package com.example.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
            VBox root = loader.load();

            // Set application icon
            Image image = new Image(Client.class.getResourceAsStream("A.png"));
            primaryStage.getIcons().add(image);

            // Create a scene and display
            Scene scene = new Scene(root, 450, 500);
            primaryStage.setTitle("Client: Chat Box");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
