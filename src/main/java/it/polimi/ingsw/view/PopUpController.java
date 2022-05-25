package it.polimi.ingsw.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class PopUpController implements Initializable{
    private String title, message;

    @FXML
    private TextArea errorMessage;
    @FXML
    private Label errorTitle;

    @FXML
    private ChoiceBox<String> errorChoiceBox;

    public static boolean confirmed = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setEditable(false);
        forceInitialize();
    }

    public void forceInitialize(){
        errorTitle.setText(title);
        errorMessage.setText(message);
    }

    @FXML
    private void delete(ActionEvent event) {
        confirmed = false;
        //PopUpLauncher.close();
    }

    @FXML
    private void confirmed(ActionEvent event) {
        confirmed = true;
        //PopUpLauncher.close();
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String msg){
        message = msg;
    }

}

