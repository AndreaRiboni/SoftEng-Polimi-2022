package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILauncher extends Application {
    public static int NOF_PLAYERS;

    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/Copertina.jpg")));
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        start_game();
    }

    public void start_game() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("gameGraphic.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1290, 690);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/Copertina.jpg")));
        stage.setTitle("Gameboard");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

   public static void main(String[] args) {
        launch();
    }
}