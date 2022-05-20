package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
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
    ImageView assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10;

    @FXML
    ImageView island_1, island_2, island_3, island_4, island_5, island_6, island_7, island_8, island_9, island_10, island_11, island_12;

    @FXML
    SubScene subscene;

    @FXML
    ImageView cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10;

    @FXML
    ChoiceBox<String> schools;
    private final String[] players = {"Player 2","Player 3"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ImageView[] crosses = { cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
        Description.setVisible(false);
        schools.getItems().addAll(players);
        schools.setValue("Player 2");
        schools.setOnAction(this::schools);
        assistant_choice.getItems().addAll(assistants_id);
        assistant_choice.setOnAction(this::get_assistant);
        for(int i = 0; i<crosses.length; i++){
            crosses[i].setVisible(false);
        }
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
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1);
        ImageView[] crosses = { cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
        ImageView[] assistants = {assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10};

        for(int i = 0; i< assistants.length; i++){
            if(Integer.parseInt(assistant_chosen) == i+1){
                assistants[i].setEffect(grayscale);
                crosses[i].setVisible(true);
            }
        }
    }

    void alignIslands(int[] islands){
        ImageView[] IslandImage = {island_1, island_2, island_3, island_4, island_5, island_6, island_7, island_8, island_9, island_10, island_11, island_12};
        float angle = 0;
        for(int i = 0; i < islands.length; i++){
            int xoff = 0;
            int yoff = 0;
            for(int o = 0; o < islands[i]; o++){
                if(o % 4 == 0 && o > 0){
                    yoff += 50;
                    xoff = 0;
                } else if(o > 0){
                    xoff += 50;
                }
                IslandImage[i].setX(subscene.getWidth()/2 + Math.cos(angle) * 200 + xoff);
                IslandImage[i].setY(subscene.getHeight()/2 + Math.sin(angle) * 200 + yoff);
            }
            angle += 2*Math.PI/islands.length;
        }
    }
}
