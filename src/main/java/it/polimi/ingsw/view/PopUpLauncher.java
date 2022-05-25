package it.polimi.ingsw.view;

import java.io.IOException;

//import it.polimi.ingsw.errorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("popUp.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.setTitle(title);
            controller.setMessage(message);
        } catch (IOException ex) {
            System.out.println("Could not instatiate the pop up window");
        }
        Scene s = new Scene(root, 412, 257);
        controller.forceInitialize();
        window = new Stage();
        window.setScene(s);
        window.setTitle(this.title);
    }


    public void show() {
        init();
        window.setAlwaysOnTop(true);
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        System.out.println("launching");
        window.showAndWait();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean choice() {
        return PopUpController.confirmed;
    }

    public void close() {
        window.close();
    }
}