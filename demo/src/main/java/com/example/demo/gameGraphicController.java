package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.io.File;
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
    SubScene subscene;

    @FXML
    ImageView cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10;

    @FXML
    ChoiceBox<String> schools;
    private final String[] players = {"Player 2","Player 3"};

    private Group[] islands;
    private final int ISLAND_SIZE = 80;

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

        //creates the 12 islands
        islands = new Group[12];
        Group islands_container = new Group();
        for(int i = 0; i < islands.length; i++){
            islands[i] = new Group();
            ImageView island_icon  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Islands/island" + i % 2 + ".png")))
            );
            island_icon.setFitHeight(ISLAND_SIZE);
            island_icon.setFitWidth(ISLAND_SIZE);

            Group students = new Group();
            for(int o = 0; o < 5; o++){
                Circle student = new Circle();
                student.setFill(Color.RED);
                student.setRadius(3);
                student.setCenterX(ISLAND_SIZE / 2);
                student.setCenterY(ISLAND_SIZE / 2);
                students.getChildren().add(student);
            }
            students.setVisible(false);

            islands[i].setOnMouseEntered(t -> {
                students.setVisible(true);
            });
            islands[i].setOnMouseExited(t -> {
                students.setVisible(false);
                System.out.println("bye bye");
            });
            islands[i].getChildren().addAll(island_icon, students);
            islands_container.getChildren().add(islands[i]);
        }
        subscene.setRoot(islands_container);
        subscene.setFill(Color.AQUAMARINE);

        //alignIslands(new int[]{4, 4, 4});
        //alignIslands(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        alignIslands(new int[]{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        //alignIslands(new int[]{1, 1, 1, 1, 8});
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

    void setIslandPos(Group island, double x, double y){
        Translate translate = new Translate();
        translate.setX(x);
        translate.setY(y);
        for(Node child : island.getChildren()){
            if(child instanceof Group){
                setIslandPos((Group) child, x, y);
            } else {
                island.getChildren().get(0).getTransforms().add(translate);
                island.getChildren().get(1).getTransforms().add(translate);
            }
        }
    }

    void resetIslandPos(Group island){
        for(Node child : island.getChildren()){
            if(child instanceof Group){
                resetIslandPos((Group) child);
            } else {
                child.getTransforms().clear();
            }
        }
    }

    void alignIslands(int[] islands_groups){
        float angle = 0;
        int count = 0;
        for(int i = 0; i < islands_groups.length; i++){
            int xoff = 0;
            int yoff = 0;
            for(int o = 0; o < islands_groups[i]; o++){
                if(o % 4 == 0 && o > 0){
                    yoff += ISLAND_SIZE * 0.9;
                    xoff = 0;
                } else if(o > 0){
                    xoff += ISLAND_SIZE * 0.7;
                }
                resetIslandPos(islands[count]);
                int temp_yoff = 0;
                if(o % 2 == 1 && islands_groups[i] > 1){
                    temp_yoff += ISLAND_SIZE * 0.45;
                }
                double x = subscene.getWidth()/2 + Math.cos(angle) * ISLAND_SIZE * 2.5 + xoff - ISLAND_SIZE/2;
                double y = subscene.getHeight()/2 + Math.sin(angle) * ISLAND_SIZE * 2.5 + yoff - ISLAND_SIZE/2 + temp_yoff;
                setIslandPos(islands[count], x, y);
                System.out.println("[i=" + count + "] x:\t" + (int)x + ",\ty:\t" + (int)y);
                count++;
            }
            angle += 2*Math.PI/islands_groups.length;
        }
    }
}
