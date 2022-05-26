package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILauncher extends Application {

    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/Copertina.jpg")));
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        LoginController loginController = fxmlLoader.getController();
        loginController.setOwnStage(stage);

/*
        fxmlLoader = new FXMLLoader(GUILauncher.class.getResource("gameGraphic.fxml"));
            Stage stage2 = new Stage();
            stage2.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/Copertina.jpg")));
            stage2.setTitle("Eriantys Match");
            try {
                stage2.setScene(new Scene(fxmlLoader.load(), 1290, 690));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage2.setResizable(true);
            stage2.show();

 */


    }

   public static void main(String[] args) {
        launch();
    }
}