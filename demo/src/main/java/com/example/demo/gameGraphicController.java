package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class gameGraphicController implements Initializable {
    @FXML
    public ChoiceBox<String> assistant_choice;
    private final String[] assistants_id = {"1", "2", "3", "4", "5","6","7","8","9","10"};
    @FXML
    Label Description;
    @FXML
    Button Info;
    @FXML
    Rectangle used_1, used_2, used_3, used_4, used_5, used_6, used_7, used_8, used_9, used_10;
    @FXML
    ChoiceBox<String> schools;
    private final String[] players = {"Player 2","Player 3"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Description.setVisible(false);
        used_1.setVisible(false);
        used_2.setVisible(false);
        used_3.setVisible(false);
        used_4.setVisible(false);
        used_5.setVisible(false);
        used_6.setVisible(false);
        used_7.setVisible(false);
        used_8.setVisible(false);
        used_9.setVisible(false);
        used_10.setVisible(false);

        schools.getItems().addAll(players);
        schools.setValue("Player 2");
        schools.setOnAction(this::schools);
        assistant_choice.getItems().addAll(assistants_id);
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
        String assistant_chosen = assistant_choice.getValue();
        System.out.println(assistant_chosen);
        switch (assistant_chosen) {
            case "1" -> used_1.setVisible(true);
            case "2" -> used_2.setVisible(true);
            case "3" -> used_3.setVisible(true);
            case "4" -> used_4.setVisible(true);
            case "5" -> used_5.setVisible(true);
            case "6" -> used_6.setVisible(true);
            case "7" -> used_7.setVisible(true);
            case "8" -> used_8.setVisible(true);
            case "9" -> used_9.setVisible(true);
            case "10" -> used_10.setVisible(true);
        }
    }
}
