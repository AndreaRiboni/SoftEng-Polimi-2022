
package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ClientGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Button button = new Button();
        button.setText("ciao");
        StackPane sp = new StackPane(button);
        Scene scene = new Scene(sp, 640, 480);
        stage.setScene(scene);
        stage.show();
        /*FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch();
    }
}
