package com.example.demo;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        start_game();
    }

    public void start_game() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameGraphic.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1270, 800);
        Stage stage = new Stage();
        stage.setTitle("Gameboard");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setMaximized(true);
    }

   public static void main(String[] args) {
        launch();
    }
}