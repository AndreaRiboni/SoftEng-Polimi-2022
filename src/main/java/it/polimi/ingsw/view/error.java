package it.polimi.ingsw;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class error {

    private static String title, message;
    private static Stage window;

    public error(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public error() {
        this.title = "";
        this.message = "";
    }

    private void init() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("popUp.fxml"));
        } catch (IOException ex) {
            System.out.println("error");
        }
        Scene s = new Scene(root, 412, 257);
        window = new Stage();
        window.setScene(s);
        window.setTitle("ERROR");
    }


    public void show() {
        init();
        window.setAlwaysOnTop(true);
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.showAndWait();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static boolean choice() {
        return errorController.confirmed;
    }


    public static void close() {
        window.close();
    }


    public static String getTitle() {
        return title;
    }

    public static String getMessage() {
        return message;
    }


    public static boolean getChoice() {
        return errorController.confirmed;
    }
}