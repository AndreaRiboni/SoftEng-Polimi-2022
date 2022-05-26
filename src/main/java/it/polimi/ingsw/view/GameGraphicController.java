package it.polimi.ingsw.view;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.*;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class GameGraphicController implements Initializable, GameBoardContainer {
    private Group[] islands, clouds;
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;
    private ImageView last_selected;
    private GameBoard model;
    public static String username;
    public static int nof_players;

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
    SplitPane splitpane;
    @FXML
    AnchorPane leftpane, rightpane;
    @FXML
    ImageView my_school;
    @FXML
    AnchorPane player_container;
    @FXML
    Pane other_schools_container;

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
            islands[i].getChildren().addAll(island_icon, students);
            islands_container.getChildren().add(islands[i]);
        }
        //creates the clouds
        Group clouds_container = new Group();
        clouds = new Group[nof_players];
        System.out.println("players: " + nof_players);
        for(int i = 0; i < clouds.length; i++){
            clouds[i] = new Group();
            ImageView cloud_icon  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Clouds/cloud" + (clouds.length-2) + ".png")))
            );
            cloud_icon.setFitHeight(ISLAND_SIZE);
            cloud_icon.setFitWidth(ISLAND_SIZE);
            GridPane students = new GridPane();
            students.setHgap(2);
            students.setVgap(2);
            clouds[i].getChildren().addAll(cloud_icon, students);
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
        System.out.println("Controller created");

        model = new GameBoard();
        try {
            model.initialize(2, 1);
            model.getPlayers()[0].setUsername("WHITE");
            username = "WHITE";
            for(Professor p : model.getProfessors())
                p.setPlayer(model.getPlayerByUsername("WHITE"));
            for(int i = 0; i < 20; i++)
            model.getPlayerByUsername("WHITE").getSchool().addStudent(Color.getRandomStudentColor(), Places.DINING_HALL);
        } catch (EriantysException e) {
            e.printStackTrace();
        }
        setGameBoard(model);
    }

    private ImageView getTowerImage(Color tower_color){
        ImageView tower = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Towers/"+Color.colorToString(tower_color)+"tower.png")))
        );
        tower.setFitWidth(Positions.TOWERS.getWidth());
        tower.setFitHeight(Positions.TOWERS.getHeight());
        return tower;
    }

    private ImageView getProfessorImage(Color prof_color){
        ImageView prof = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Professors/" + Color.colorToString(prof_color) + "prof.png")))
        );
        prof.setFitWidth(Positions.PROFESSORS.getWidth());
        prof.setFitHeight(Positions.PROFESSORS.getHeight());
        return prof;
    }

    private ImageView getStudentImage(Color color){
        ImageView student = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Students/"+Color.colorToString(color)+"stud.png")))
        );
        student.setFitWidth(Positions.DINING_HALL_STUDENTS.getWidth());
        student.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return student;
    }

    private Label getWhiteLabel(String text){
        Label l = new Label(text);
        l.setTextFill(Paint.valueOf("#ffffff"));
        return l;
    }

    private void copyOtherSchools(){
        other_schools_container.getChildren().clear();
        GridPane grid = new GridPane();
        Player[] others = model.getPlayersNotCalledLike(username);
        VBox[] others_container = new VBox[others.length];
        for(int i = 0; i < others.length; i++){
            others_container[i] = new VBox();
            //User
            others_container[i].getChildren().add(getWhiteLabel(others[i].getUsername() + "'s school"));
            //Towers
            others_container[i].getChildren().add(getWhiteLabel("Tower Hall"));
            HBox towers = new HBox();
            for(int t = 0; t < others[i].getNumberOfUnplacedTowers(); t++){
                towers.getChildren().add(getTowerImage(others[i].getColor()));
            }
            others_container[i].getChildren().add(towers);
            //Professors
            others_container[i].getChildren().add(getWhiteLabel("Professors Room"));
            HBox professors = new HBox();
            for(int t = 0; t < model.getProfessors().length; t++){
                Color prof_col = model.getProfessors()[t].getColor();
                ImageView prof_img = getProfessorImage(prof_col);
                if(!model.getProfessors()[t].getPlayer().equals(others[i]))
                    applySelectedEffect(prof_img);
                professors.getChildren().add(prof_img);
            }
            others_container[i].getChildren().add(professors);
            //Dining hall
            others_container[i].getChildren().add(getWhiteLabel("Dining Hall"));
            Map<Color, Integer> dining = others[i].getDiningStudents();
            for(Color col : Color.getStudentColors()) {
                HBox diningbox = new HBox();
                if(dining.getOrDefault(col, 0) == 0){
                    ImageView empty_stud = getStudentImage(col);
                    applySelectedEffect(empty_stud);
                    diningbox.getChildren().add(empty_stud);
                } else {
                    for (int t = 0; t < dining.getOrDefault(col, 0); t++) {
                        diningbox.getChildren().add(getStudentImage(col));
                    }
                }
                others_container[i].getChildren().add(diningbox);
            }
            //Entrance
            others_container[i].getChildren().add(getWhiteLabel("Entrance"));
            Map<Color, Integer> entrance = others[i].getEntranceStudents();
            HBox entrancebox = new HBox();
            for(Color col : Color.getStudentColors()) {
                for (int t = 0; t < entrance.getOrDefault(col, 0); t++) {
                    entrancebox.getChildren().add(getStudentImage(col));
                }
            }
            others_container[i].getChildren().add(entrancebox);
            //Final
            grid.add(others_container[i], i, 0);
        }
        grid.setGridLinesVisible(true);
        grid.setHgap(3);
        grid.setVgap(3);
        other_schools_container.getChildren().add(grid);
    }

    @Override
    public void setGameBoard(GameBoard model){
        System.out.println("Received gameboard");
        this.model = model;
        Platform.runLater(() -> {
            //copying my school
            copySchool(model.getPlayerByUsername(username).getSchool(), true);
            //copying islands and clouds
            copyIslands();
            copyClouds();
            System.out.println("My user is " + username);
            copyOtherSchools();
        });
    }

    private void drawTowers(Group school_elements, School school){
        Color tower_color = school.getTowerColor();
        int xoff = 0, yoff = 0;
        for(int i = 0; i < school.getNumberOfTowers(); i++){
            ImageView tower = getTowerImage(tower_color);
            tower.setTranslateX(Positions.TOWERS.getX() + xoff * Positions.TOWERS.getXOff());
            tower.setTranslateY(Positions.TOWERS.getY() + yoff * Positions.TOWERS.getYOff());
            school_elements.getChildren().add(tower);
            xoff++;
            if(i == 3){
                yoff++;
                xoff = 0;
            }
        }
    }

    private void drawProfessors(Group school_elements, Color[] sorted_stud_colors){
        for(int i = 0; i < sorted_stud_colors.length; i++){
            Player reference = model.getProfFromColor(sorted_stud_colors[i]).getPlayer();
            if(reference != null && reference.getUsername().equals(username)) {
                ImageView prof = getProfessorImage(sorted_stud_colors[i]);
                prof.setTranslateX(Positions.PROFESSORS.getX() + i * Positions.PROFESSORS.getXOff());
                prof.setTranslateY(Positions.PROFESSORS.getY() + i * Positions.PROFESSORS.getYOff());
                school_elements.getChildren().add(prof);
            }
        }
    }

    private void drawDiningHall(Group school_elements, School school) {
        float off = Positions.DINING_HALL_STUDENTS.getXOff();
        float[] xoff = {0, off, 2*off, 3*off, 4*off};
        float[] yoff = new float[5];
        int index_col = 0;
        Map<Color, Integer> students = school.getDiningStudents();
        for(Color color : students.keySet()){ //for each color
            for(int i = 0; i < students.getOrDefault(color, 0); i++){ //as many as the nof students of that color
                ImageView student = getStudentImage(color);
                student.setTranslateX(Positions.DINING_HALL_STUDENTS.getX() + xoff[index_col]);
                student.setTranslateY(Positions.DINING_HALL_STUDENTS.getY() + yoff[index_col]);
                school_elements.getChildren().add(student);
                yoff[index_col] += Positions.DINING_HALL_STUDENTS.getYOff();
            }
            index_col++;
        }
    }

    private void drawEntrance(Group school_elements, School school){
        int xoff = 0, yoff = 0, count = 0;
        Map<Color, Integer> students = school.getEntranceStudents();
        for(Color col : students.keySet()){
            for(int i = 0; i < students.getOrDefault(col, 0); i++){
                ImageView student = new ImageView(
                        new Image(String.valueOf(getClass().getResource("/Students/"+Color.colorToString(col)+"stud.png")))
                );
                student.setFitWidth(Positions.ENTRANCE_STUDENTS.getWidth());
                student.setFitHeight(Positions.ENTRANCE_STUDENTS.getHeight());
                student.setTranslateX(Positions.ENTRANCE_STUDENTS.getX() + xoff * Positions.ENTRANCE_STUDENTS.getXOff());
                student.setTranslateY(Positions.ENTRANCE_STUDENTS.getY() + yoff * Positions.ENTRANCE_STUDENTS.getYOff());
                student.setOnMouseClicked(e -> selectMovingSubject(student));
                school_elements.getChildren().add(student);
                xoff++;
                if(count == 4){
                    yoff++;
                    xoff = 0;
                }
                count++;
            }
        }
    }

    private void copyClouds(){
        alignClouds();
        for(int i = 0; i < nof_players; i++){
            try {
                Map<Color, Integer> cloud_stud = model.getCloud(i).getStudents();
                GridPane cloud_grid = (GridPane) clouds[i].getChildren().get(1);
                int count = 0;
                for(Color color : cloud_stud.keySet()) {
                    for (int o = 0; o < cloud_stud.getOrDefault(color, 0); o++) {
                        ImageView student = new ImageView(
                                new Image(String.valueOf(getClass().getResource("/Students/" + Color.colorToString(color) + "stud.png")))
                        );
                        student.setFitHeight(STUDENT_SIZE * 5);
                        student.setFitWidth(STUDENT_SIZE * 5);
                        cloud_grid.add(student, count % 2, count / 2);
                        count++;
                    }
                }
            } catch (EriantysException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyIslands(){
        int count = 0;
        int[] island_groups = new int[model.getNofGroupsOfIslands()];
        int linked = 0;
        for(int i = 0; i < island_groups.length; i+=linked){
            linked = model.getIslands()[count].countNextLinked();
            island_groups[i] = linked;
            count += linked;
        }
        alignIslands(island_groups);
    }

    private void copySchool(School school, boolean mine){
        AnchorPane pane = mine ? player_container : null;
        //removes each non-imageview child
        if(pane.getChildren().size() > 2) pane.getChildren().remove(2);
        Group school_elements = new Group();
        //Towers
        drawTowers(school_elements, school);
        //Colors in the correct order
        Color[] sorted_stud_colors = {Color.GREEN, Color.RED, Color.YELLOW, Color.PINK, Color.BLUE};
        //Professors
        drawProfessors(school_elements, sorted_stud_colors);
        //Students
        drawDiningHall(school_elements, school);
        drawEntrance(school_elements, school);
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
    public void notifyResponse(Action action) {
        System.out.println("Received action " + action.getGamePhase());
    }

    @Override
    public void notifyResponse(List<GamePhase> gamephases) {
        System.out.println("Received gamephases list");
    }

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

    private void fillIslands(){
        Island[] model_islands = model.getIslands();
        for(int i = 0; i < model_islands.length; i++){
            GridPane island_grid = (GridPane) islands[i].getChildren().get(1);
            Map<Color, Integer> island_studs = model_islands[i].getStudents();
            int total = GenericUtils.sumValues(island_studs);
            int count = 0;
            for(Color color : island_studs.keySet()) {
                for (int o = 0; o < island_studs.getOrDefault(color, 0); o++) {
                    ImageView student = new ImageView(
                            new Image(String.valueOf(getClass().getResource("/Students/" + Color.colorToString(color) + "stud.png")))
                    );
                    student.setFitHeight(STUDENT_SIZE);
                    student.setFitWidth(STUDENT_SIZE);
                    island_grid.add(student, count % 8, count / 8);
                    count++;
                }
            }
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
        fillIslands();
    }
}
