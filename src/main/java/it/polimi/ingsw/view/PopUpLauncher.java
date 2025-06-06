package it.polimi.ingsw.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import it.polimi.ingsw.errorController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class PopUpLauncher {
    private Stage window;
    private PopUpController controller;
    private String title, message;

    public PopUpLauncher(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public PopUpLauncher() {
        this("", "");
    }

    private void init() {
        Pane root = null;
        window = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popUp.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.setTitle(title);
            controller.setMessage(message);
            controller.setOwnStage(window);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene s = new Scene(root, 412, 257);
        controller.forceInitialize();
        window.setScene(s);
        window.setTitle(this.title);
    }

    public void show() {
        Platform.runLater(() -> {
            init();
            window.setAlwaysOnTop(true);
            window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();
        });
    }

    public void showAndByeBye(){
        Platform.runLater(() -> {
            init();
            window.setOnCloseRequest(e->{
                Platform.exit();
                System.exit(0);
            });
            window.setAlwaysOnTop(true);
            window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();
        });
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void close() {
        window.close();
    }
}