package com.example.demo;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;

    @FXML
    SplitPane splitpane;
    @FXML
    AnchorPane leftpane, rightpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        rightpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
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
        String[] colors = {"yellow", "pink", "green", "blue", "red"};
        for(int i = 0; i < islands.length; i++){
            islands[i] = new Group();
            ImageView island_icon  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Islands/island" + i % 2 + ".png")))
            );
            island_icon.setFitHeight(ISLAND_SIZE);
            island_icon.setFitWidth(ISLAND_SIZE);

            GridPane students = new GridPane();
            students.setHgap(2);
            students.setVgap(2);
            int nof_stud = (int)(Math.random() * 20);

            for(int o = 0; o < nof_stud; o++){
                int index = (int)(Math.random() * colors.length);
                ImageView student = new ImageView(
                        new Image(String.valueOf(getClass().getResource("/Students/"+colors[index]+"stud.png")))
                );
                student.setFitHeight(STUDENT_SIZE);
                student.setFitWidth(STUDENT_SIZE);
                students.add(student, o%8, o/8);
            }

            islands[i].getChildren().addAll(island_icon, students);
            islands_container.getChildren().add(islands[i]);
        }
        Pane pane = new Pane(islands_container);
        pane.setPrefWidth(subscene.getWidth() * 2);
        pane.setPrefHeight(subscene.getHeight() * 2);
        pane.setTranslateX(-pane.getWidth() / 2);
        pane.setTranslateY(-pane.getHeight() / 2);
        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        BorderPane bp = new BorderPane(sp);
        subscene.setRoot(bp);
        zoom(pane);
        forceZoom(pane, 300);
        navigate(pane);

        subscene.setOnMouseClicked(e -> {
            List<Integer> fake_islands = new ArrayList<>();
            int sum = 0;
            Random rand = new Random();
            do {
                if(fake_islands.size() == 11) {
                    fake_islands.add(12 - sum);
                    sum = 12;
                }
                int val = (int) (Math.log(1-rand.nextDouble())/(-0.5));
                if(val == 0 || sum + val > 12) continue;
                sum += val;
                fake_islands.add(val);
            } while (sum < 12);
            int[] array = new int[fake_islands.size()];
            for(int i = 0; i < fake_islands.size(); i++){
                array[i] = fake_islands.get(i);
            }
            System.out.println(fake_islands);
            alignIslands(array);
        });
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

    private void zoom(Pane pane) {
        pane.setOnScroll(
                event -> {
                    forceZoom(pane, event.getDeltaY());
                    event.consume();
                });
    }

    private void forceZoom(Pane pane, double deltaY){
        double zoomFactor = 1.05;
        if (deltaY < 0) {
            zoomFactor = 0.95;
        }
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
    }

    private void navigate(Pane pane){
        final double[] start = new double[2];
        pane.setOnDragDetected(event -> {
            start[0] = event.getX();
            start[1] = event.getY();
            event.consume();
        });
        pane.setOnMouseDragged(event -> {
            Translate t = new Translate();
            t.setX((event.getX() - start[0]) / 20);
            t.setY((event.getY() - start[1]) / 20);
            pane.getTransforms().add(t);
        });
    }

    private void setIslandPos(Group island, double x, double y){
        for(Node child : island.getChildren()){
            if(child instanceof Group){
                setIslandPos((Group) child, x, y);
            } else {
                if(child instanceof GridPane){
                    x += ISLAND_SIZE / 5;
                    y += ISLAND_SIZE / 5;
                }
                TranslateTransition translate = new TranslateTransition();
                translate.setDuration(Duration.millis(1000));
                translate.setFromX(child.getTranslateX());
                translate.setFromY(child.getTranslateY());
                translate.setToX(x);
                translate.setToY(y);
                translate.setNode(child);
                translate.play();
                //child.setLayoutX(x);
                //child.setLayoutY(y);
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
                //resetIslandPos(islands[count]);
                int temp_yoff = 0;
                if(o % 2 == 1 && islands_groups[i] > 1){
                    temp_yoff += ISLAND_SIZE * 0.45;
                }
                double x = subscene.getWidth()/2 + Math.cos(angle) * ISLAND_SIZE * 4 + xoff - ISLAND_SIZE/2;
                double y = subscene.getHeight()/2 + Math.sin(angle) * ISLAND_SIZE * 4 + yoff - ISLAND_SIZE/2 + temp_yoff;
                setIslandPos(islands[count], x, y);
                //System.out.println("[i=" + count + "] x:\t" + (int)x + ",\ty:\t" + (int)y);
                count++;
            }
            angle += 2*Math.PI/islands_groups.length;
        }
    }
}
