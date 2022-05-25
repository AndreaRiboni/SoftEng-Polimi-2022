package it.polimi.ingsw.view;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GameBoardContainer;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GameGraphicController implements Initializable, GameBoardContainer {
    private Group[] islands, clouds;
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;
    private ImageView last_selected;
    private GameBoard model;
    private String username;

    @FXML
    ChoiceBox<String> assistant_choice;
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
    @FXML
    SplitPane splitpane;
    @FXML
    AnchorPane leftpane, rightpane;
    @FXML
    ImageView my_school;
    @FXML
    AnchorPane player_container;

    private final String[] players = {"Player 2","Player 3"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //lock tabpane
        leftpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        rightpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        //put images into an array
        ImageView[] crosses = { cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
        //show descriptions and images
        Description.setVisible(false);
        schools.getItems().addAll(players);
        schools.setValue("Player 2");
        assistant_choice.getItems().addAll(assistants_id);
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
            /*int nof_stud = (int)(Math.random() * 20);
            for(int o = 0; o < nof_stud; o++){
                int index = (int)(Math.random() * colors.length);
                ImageView student = new ImageView(
                        new Image(String.valueOf(getClass().getResource("/Students/"+colors[index]+"stud.png")))
                );
                student.setFitHeight(STUDENT_SIZE);
                student.setFitWidth(STUDENT_SIZE);
                students.add(student, o%8, o/8);
            }
             */
            islands[i].getChildren().addAll(island_icon, students);
            islands_container.getChildren().add(islands[i]);
        }
        //creates the clouds
        Group clouds_container = new Group();
        clouds = new Group[2];
        for(int i = 0; i < clouds.length; i++){
            clouds[i] = new Group();
            ImageView cloud_icon  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Clouds/cloud" + (clouds.length-2) + ".png")))
            );
            cloud_icon.setFitHeight(ISLAND_SIZE);
            cloud_icon.setFitWidth(ISLAND_SIZE);
            clouds[i].getChildren().add(cloud_icon);
            clouds_container.getChildren().add(clouds[i]);
        }
        Pane pane = new Pane(islands_container, clouds_container);
        pane.setPrefWidth(subscene.getWidth() * 2);
        pane.setPrefHeight(subscene.getHeight() * 2);
        pane.setTranslateX(-pane.getWidth() / 2);
        pane.setTranslateY(-pane.getHeight() / 2);
        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        BorderPane bp = new BorderPane(sp);
        subscene.setRoot(bp);
        addZoomListener(pane);
        forceZoom(pane, 300, true);
        applyNavigationListener(pane);

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
        alignIslands(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        alignClouds();
        my_school.setOnMouseClicked(e->{
            System.out.println(e.getX() + ", " + e.getY());
        });

        model = new GameBoard();
        try {
            model.initialize(2, 1);
        } catch (EriantysException e) {
            e.printStackTrace();
        }
        username = "white";
        setGameBoard(model);
    }

    @Override
    public void setGameBoard(GameBoard model){
        this.model = model;
        //copying my school
        //copySchool(model.getPlayerByUsername(username).getSchool(), true);
        copySchool(model.getPlayers()[0].getSchool(), true);
    }

    private void copySchool(School school, boolean mine){
        AnchorPane pane = mine ? player_container : null;
        //removes each non-imageview child
        if(pane.getChildren().size() > 2) pane.getChildren().remove(2);
        Group school_elements = new Group();
        //TODO: towers (needs pngs)
        //Colors in the correct order
        Color[] sorted_stud_colors = {Color.GREEN, Color.RED, Color.YELLOW, Color.PINK, Color.BLUE};
        //Professors TODO: need professors pngs
        for(int i = 0; i < sorted_stud_colors.length; i++){
            ImageView prof = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Students/"+Color.colorToString(sorted_stud_colors[i])+"stud.png")))
            );
            prof.setFitWidth(Positions.PROFESSORS.getSize());
            prof.setFitHeight(Positions.PROFESSORS.getSize());
            prof.setTranslateX(Positions.PROFESSORS.getX() + i * Positions.PROFESSORS.getXOff());
            prof.setTranslateY(Positions.PROFESSORS.getY() + i * Positions.PROFESSORS.getYOff());
            prof.setOnMouseClicked(e -> selectMovingSubject(prof));
            school_elements.getChildren().add(prof);
        }
        pane.getChildren().add(school_elements);
    }

    private void selectMovingSubject(ImageView img) {
        applySelectedEffect(img);
        if(last_selected != null){
            removeSelectedEffect(last_selected);
        }
        last_selected = img;
    }

    @Override
    public void notifyResponse(Action action) {}

    public void toggleDescriptionVisibility(){
        Description.setVisible(!Description.isVisible());
    }

    private void applySelectedEffect(ImageView img){
        ColorAdjust selected_effect = new ColorAdjust();
        selected_effect.setSaturation(0.2);
        selected_effect.setBrightness(0.8);
        img.setEffect(selected_effect);
    }

    private void removeSelectedEffect(ImageView img){
        img.setEffect(null);
    }

    @FXML
    void applyPlayedAssistCardEffect(){
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

    private void addZoomListener(Pane pane) {
        pane.setOnScroll(
                event -> {
                    forceZoom(pane, event.getDeltaY(), false);
                    event.consume();
                });
    }

    private void forceZoom(Pane pane, double deltaY, boolean start){
        double zoomFactor;
        if(start)  zoomFactor = 0.3;
        else{ zoomFactor = 1.05;}
        if (deltaY < 0) {
            zoomFactor = 0.95;
        }
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
    }

    private void applyNavigationListener(Pane pane){
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
            }
        }
    }

    private void alignClouds(){
        float angle = 0;
        for (Group cloud : clouds) {
            double x = subscene.getWidth() / 2 + Math.cos(angle) * ISLAND_SIZE - ISLAND_SIZE / 2;
            double y = subscene.getHeight() / 2 + Math.sin(angle) * ISLAND_SIZE - ISLAND_SIZE / 2;
            setIslandPos(cloud, x, y);
            angle += 2 * Math.PI / clouds.length;
        }
    }

    private void alignIslands(int[] islands_groups){
        float angle = 0;
        int count = 0;
        for (int islands_group : islands_groups) {
            int xoff = 0;
            int yoff = 0;
            for (int o = 0; o < islands_group; o++) {
                if (o % 4 == 0 && o > 0) {
                    yoff += ISLAND_SIZE * 0.9;
                    xoff = 0;
                } else if (o > 0) {
                    xoff += ISLAND_SIZE * 0.7;
                }
                //resetIslandPos(islands[count]);
                int temp_yoff = 0;
                if (o % 2 == 1) {
                    temp_yoff += ISLAND_SIZE * 0.45;
                }
                double x = subscene.getWidth() / 2 + Math.cos(angle) * ISLAND_SIZE * 4 + xoff - ISLAND_SIZE / 2;
                double y = subscene.getHeight() / 2 + Math.sin(angle) * ISLAND_SIZE * 4 + yoff - ISLAND_SIZE / 2 + temp_yoff;
                setIslandPos(islands[count], x, y);
                //System.out.println("[i=" + count + "] x:\t" + (int)x + ",\ty:\t" + (int)y);
                count++;
            }
            angle += 2 * Math.PI / islands_groups.length;
        }
    }
}
