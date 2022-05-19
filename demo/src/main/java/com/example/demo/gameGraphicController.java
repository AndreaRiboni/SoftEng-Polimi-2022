package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class gameGraphicController implements Initializable {
    @FXML
    public ChoiceBox<String> assistant_choice;
    private final String[] assistants_id = {"1", "2", "3", "4"};
    @FXML
    Label Description;
    @FXML
    Button Info;
    @FXML
    Label Used;
    @FXML
    ChoiceBox<String> schools;
    private final String[] players = {"Player 2","Player 3"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Description.setVisible(false);
        Used.setVisible(false);
        schools.getItems().addAll(players);
        schools.setValue("Player 2");
        schools.setOnAction(this::schools);
        assistant_choice.getItems().addAll(assistants_id);
        assistant_choice.setValue("1");
        assistant_choice.setOnAction(this::get_assistant);
    }

    public void schools(ActionEvent event){
        String players = schools.getValue();
    }

    public void get_assistant(ActionEvent event){
        String assistant_id = assistant_choice.getValue();
    }
    public void changevisible(){
        if(Description.isVisible()){Description.setVisible(false);}
        else{Description.setVisible(true);}
    }

    public void used(){
        System.out.println(assistant_choice.getValue());
        if(assistant_choice.getValue() == "1") Used.setVisible(true);
    }
}
