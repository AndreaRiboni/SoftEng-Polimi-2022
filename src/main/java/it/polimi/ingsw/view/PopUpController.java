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
    private String title, message, chosen;
    private List<String> str_choices;
    private Stage owned;

    @FXML
    private TextArea errorMessage;
    @FXML
    private Label errorTitle;
    @FXML
    private ChoiceBox<String> choices;
    @FXML
    private Button choose_button;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setEditable(false);
        str_choices = new ArrayList<>();
        forceInitialize();
    }

    public void forceInitialize(){
        errorTitle.setText(title);
        errorMessage.setText(message);
        choices.setDisable(str_choices.isEmpty());
        choose_button.setDisable(str_choices.isEmpty());
        for(String s : str_choices){
            choices.getItems().add(s);
        }
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMessage(String msg){
        message = msg;
    }

    public void setChoices(List<String> choices) {
        str_choices = choices;
    }

    public void setOwnStage(Stage s){
        owned = s;
    }

    public String getChosen(){
        return chosen;
    }

    @FXML
    private void choose(){
        chosen = choices.getValue();
        owned.close();
    }
}

