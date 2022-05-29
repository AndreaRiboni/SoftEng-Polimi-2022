package it.polimi.ingsw.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class PopUpController implements Initializable{
    private String title, message;
    private Stage owned;

    @FXML
    private TextArea errorMessage;
    @FXML
    private Label errorTitle;
    @FXML
    private Button choose_button;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setEditable(false);
        forceInitialize();
    }

    public void forceInitialize(){
        errorTitle.setText(title);
        errorMessage.setText(message);
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String msg){
        message = msg;
    }

    public void setOwnStage(Stage s){
        owned = s;
    }
}

