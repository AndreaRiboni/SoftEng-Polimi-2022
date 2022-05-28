package it.polimi.ingsw.view;

import it.polimi.ingsw.global.MessageSender;
import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.*;
import it.polimi.ingsw.model.utils.packets.StudentLocation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.SocketException;
import java.net.URL;
import java.util.*;

public class GameGraphicController implements Initializable, GameBoardContainer {
    private Group[] islands, clouds;
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;
    private Node last_selected;
    private GameBoard model;
    public static String username;
    public static int nof_players;
    public static MessageSender msg;
    private Action last_sent;
    private List<StudentLocation> move_students;
    private boolean disabled, started;
    private ImageView mothernature_img;

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
    @FXML
    Label TurnStatus;
    @FXML
    ProgressBar TurnStatusBar;

    private final String[] players = {"Player 2","Player 3"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        started = false;
        disabled = false;
        //lock tabpane
        leftpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        rightpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.5));
        //put images into an array
        ImageView[] crosses = {cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
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
        mothernature_img = getMotherNatureImage();
        for(int i = 0; i < islands.length; i++){
            islands[i] = new Group();
            ImageView island_icon  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Islands/island" + i % 2 + ".png")))
            );
            island_icon.setFitHeight(ISLAND_SIZE);
            island_icon.setFitWidth(ISLAND_SIZE);
            island_icon.getProperties().put("type", "island");
            island_icon.getProperties().put("index", i);
            GridPane students = new GridPane();
            students.getProperties().put("type", "island-students");
            students.setHgap(1);
            students.setVgap(2);
            islands[i].getChildren().addAll(island_icon, students);
            islands_container.getChildren().add(islands[i]);
            //Mother nature menu
            ContextMenu mn_menu = new ContextMenu();
            MenuItem request_mn_item = new MenuItem("Move mother nature here");
            int finalI = i;
            request_mn_item.setOnAction(e -> {
                sendMotherNatureRequest(finalI);
            });
            mn_menu.getItems().add(request_mn_item);
            students.setOnContextMenuRequested(e -> mn_menu.show(island_icon, e.getScreenX(), e.getScreenY()));
            //adding mouse management to island
            int finalI1 = i;
            islands[i].setOnMouseClicked(e -> {
                if(!disabled) {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        selectMovingSubject(islands[finalI1]);
                    } else {
                        mn_menu.show(islands[finalI1], e.getScreenX(), e.getScreenY());
                    }
                } else {
                    showWrongTurnPopUp();
                }
            });
            islands[i].getProperties().put("type", "island");
            islands[i].getProperties().put("index", i);
        }
        //creates the clouds
        Group clouds_container = new Group();
        clouds = new Group[nof_players];
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
            //Drain cloud menu
            ContextMenu dc_menu = new ContextMenu();
            MenuItem request_dc_item = new MenuItem("Retrieve these students");
            int finalI = i;
            request_dc_item.setOnAction(e -> {
                sendDrainCloudRequest(finalI);
            });
            dc_menu.getItems().add(request_dc_item);
            cloud_icon.setOnContextMenuRequested(e -> dc_menu.show(cloud_icon, e.getScreenX(), e.getScreenY()));
            students.setOnContextMenuRequested(e -> dc_menu.show(students, e.getScreenX(), e.getScreenY()));
        }
        Pane pane = new Pane(islands_container, clouds_container, new Group(mothernature_img));
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
        move_students = new ArrayList<>();
    }

    public void setStarted(){
        started = true;
    }

    private ImageView getTowerImage(Color tower_color){
        ImageView tower = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Towers/"+Color.colorToString(tower_color)+"tower.png")))
        );
        tower.setFitWidth(Positions.TOWERS.getWidth());
        tower.setFitHeight(Positions.TOWERS.getHeight());
        return tower;
    }

    private ImageView getMotherNatureImage(){
        ImageView mn = new ImageView(
                new Image(String.valueOf(getClass().getResource("/MotherNature/mothernature.gif")))
        );
        mn.setFitWidth(ISLAND_SIZE);
        mn.setFitHeight(ISLAND_SIZE);
        mn.getProperties().put("mothernature", true);
        mn.setEffect(new Glow(1));
        return mn;
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

    //TODO: FIX
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
            int tot_tower = others[i].getNumberOfPlacedTowers() + others[i].getNumberOfUnplacedTowers();
            for(int t = 0; t < tot_tower; t++){ //TODO: FIX
                ImageView tower_img = getTowerImage(others[i].getColor());
                if(t >= others[i].getNumberOfUnplacedTowers()){
                    applyNotPresentEffect(tower_img);
                }
                towers.getChildren().add(tower_img);
            }
            others_container[i].getChildren().add(towers);
            //Professors
            others_container[i].getChildren().add(getWhiteLabel("Professors Room"));
            HBox professors = new HBox();
            for(int t = 0; t < model.getProfessors().length; t++){
                Color prof_col = model.getProfessors()[t].getColor();
                ImageView prof_img = getProfessorImage(prof_col);
                Player detained = model.getProfessors()[t].getPlayer();
                if(detained == null || !detained.equals(others[i]))
                    applyNotPresentEffect(prof_img);
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
                    applyNotPresentEffect(empty_stud);
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
        System.out.println(model);
        this.model = model;
        move_students.clear();
        Platform.runLater(() -> {
            //copying my school
            copySchool(model.getPlayerByUsername(username).getSchool(), true);
            //copying islands and clouds
            try {
                copyIslands();
            } catch (EriantysException e) {
                e.printStackTrace();
            }
            copyClouds();
            copyOtherSchools();
            copyMotherNature();
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

    private void drawDiningHall(Group school_elements, School school, Color[] sorted_stud_colors) {
        float off = Positions.DINING_HALL_STUDENTS.getXOff();
        float[] xoff = {0, off, 2*off, 3*off, 4*off};
        float[] yoff = new float[5];
        int index_col = 0;
        Map<Color, Integer> students = school.getDiningStudents();
        for(Color color : sorted_stud_colors){ //for each color
            for(int i = 0; i < students.getOrDefault(color, 0); i++){ //as many as the nof students of that color
                ImageView student = getStudentImage(color);
                student.setTranslateX(Positions.DINING_HALL_STUDENTS.getX() + xoff[index_col]);
                student.setTranslateY(Positions.DINING_HALL_STUDENTS.getY() + yoff[index_col]);
                school_elements.getChildren().add(student);
                yoff[index_col] += Positions.DINING_HALL_STUDENTS.getYOff();
            }
            index_col++;
        }
        Rectangle dining_area = new Rectangle();
        dining_area.setOpacity(0);
        dining_area.setX(223);
        dining_area.setY(250);
        dining_area.setWidth(200);
        dining_area.setHeight(290);
        ImageView iv = new ImageView();
        iv.getProperties().put("type", "dining");
        dining_area.setOnMouseClicked(e -> {if(!disabled) selectMovingSubject(iv); else showWrongTurnPopUp();});
        school_elements.getChildren().add(dining_area);
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
                student.setOnMouseClicked(e -> {if(!disabled) selectMovingSubject(student); else showWrongTurnPopUp();});
                student.getProperties().put("type", "student");
                student.getProperties().put("color", col);
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

    private void copyMotherNature(){
        int island_index = model.getMotherNature().getIslandIndex();
        for(int i = 0; i < model.getIslands().length; i++){
            boolean visibility = i == island_index;
            for(Node n : islands[i].getChildren()){
                if(n instanceof ImageView && n.getProperties().get("mothernature")!=null)
                n.setVisible(visibility);
            }
        }
    }

    private void copyClouds(){
        alignClouds();
        for(int i = 0; i < nof_players; i++){
            try {
                Map<Color, Integer> cloud_stud = model.getCloud(i).getStudents();
                GridPane cloud_grid = (GridPane) clouds[i].getChildren().get(1);
                cloud_grid.getChildren().clear();
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

    private void copyIslands() throws EriantysException {
        int count_islands = 0;
        int count_groups = 0;
        int[] island_groups = new int[model.getNofGroupsOfIslands()];
        //BiInteger[] island = new BiInteger[model.getNofGroupsOfIslands()];
        int linked;
        while(count_islands < model.getIslands().length && count_groups < model.getNofGroupsOfIslands()){
            Island isl = model.getIslands()[count_islands];
            if(!isl.hasPrevious()){
                linked = isl.countNextLinked();
                island_groups[count_groups] = linked;
                count_islands += linked;
                count_groups++;
            }else{
                count_islands++;
            }
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
        drawDiningHall(school_elements, school, sorted_stud_colors);
        drawEntrance(school_elements, school);
        pane.getChildren().add(school_elements);
    }

    private void selectMovingSubject(Node node) {
        if(move_students.isEmpty()) move_students.add(new StudentLocation()); //adding first packet eventually
        if(node.getProperties().get("type").equals("student")){
            if(node instanceof ImageView) applySelectedEffect((ImageView) node);
            if(move_students.get(move_students.size()-1).getIsland_index() == -1){ //no island selected yet
                move_students.get(move_students.size()-1).setColor((ImageView) node); //reset last color
                if(last_selected != null && last_selected instanceof ImageView){
                    removeSelectedEffect((ImageView) last_selected);
                }
            } else {
                move_students.add(new StudentLocation(-1, node));
            }
        } else if(node.getProperties().get("type").equals("island")) {
            //it's an island: create the action and move the student there
            int island_index = (Integer) node.getProperties().get("index");
            if(last_selected != null) { //if we already selected a student we can set its island destination
                move_students.get(move_students.size() - 1).setIsland_index(island_index);
                //lock this student
                move_students.get(move_students.size() - 1).getColor().setOnMouseClicked(e->{});
            }
            last_selected = null;
        } else { //dining
            if(last_selected != null) //if we already selected a student we can set its island destination
                move_students.get(move_students.size()-1).setIsland_index(100);
            last_selected = null;
        }
        if(move_students.size() == nof_players + 1 && move_students.get(move_students.size()-1).getIsland_index() != -1){
            sendMoveStudentsRequest();
        }
        last_selected = node;
        System.out.println(move_students);
    }

    private void manageResponse(){
        if(last_sent == null) return;
        switch(last_sent.getGamePhase()){
            case DRAW_ASSIST_CARD:
                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(-1);
                ImageView[] crosses = { cross_1, cross_2, cross_3, cross_4, cross_5, cross_6, cross_7, cross_8, cross_9, cross_10};
                ImageView[] assistants = {assistant_1, assistant_2, assistant_3, assistant_4, assistant_5, assistant_6, assistant_7, assistant_8, assistant_9, assistant_10};
                for(int i = 0; i< assistants.length; i++){
                    if(last_sent.getAssistCardIndex() == i){
                        assistants[i].setEffect(grayscale);
                        crosses[i].setVisible(true);
                    }
                }
                break;
        }
    }

    @Override
    public void notifyResponse(Action action) {
        if(!started) return;
        System.out.println("Received action " + action.getGamePhase());
        Platform.runLater(() -> {
            if(action.getGamePhase().equals(GamePhase.CORRECT)){
                disabled = true;
                TurnStatus.setText("Wait until the other players have finished their turn");
                TurnStatusBar.setVisible(true);
                manageResponse();
            } else if(action.getGamePhase().equals(GamePhase.ERROR_PHASE)){
                disabled = false;
                PopUpLauncher error_phase = new PopUpLauncher();
                error_phase.setTitle("Something went wrong");
                error_phase.setMessage(action.getErrorMessage());
                error_phase.show();
                setGameBoard(model);
                move_students.clear();
            }
        });
    }

    private void showWrongTurnPopUp(){
        Platform.runLater(() -> {
            PopUpLauncher wrongturn = new PopUpLauncher("It's not your turn", "Wait until the other players have finished");
            wrongturn.show();
        });
    }

    @Override
    public void notifyResponse(List<GamePhase> gamephases) {
        Platform.runLater(() -> {
            TurnStatus.setText("It's your turn!");
            disabled = false;
            TurnStatusBar.setVisible(false);
            if(gamephases.contains(GamePhase.PUT_ON_CLOUDS)){
                sendPutOnCloudsRequest();
            }
            //TODO: assegno last_sent ogni volta + se mando drain_cloud e ricevo correct e ottengo questa lista allora invio put_on_clouds
        });
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

    private void applyNotPresentEffect(ImageView img){
        img.setOpacity(0.5);
        ColorAdjust selected_effect = new ColorAdjust();
        selected_effect.setSaturation(0.1);
        img.setEffect(selected_effect);
    }

    private void removeSelectedEffect(ImageView img){
        img.setEffect(null);
    }

    @FXML
    void sendAssistCardRequest() throws SocketException {
        String assistant_chosen = assistant_choice.getValue();
        //sendAssistCard
        Action assist_card = new Action();
        assist_card.setGamePhase(GamePhase.DRAW_ASSIST_CARD);
        assist_card.setAssistCardIndex(Integer.parseInt(assistant_chosen) - 1);
        msg.send(assist_card);
        last_sent = assist_card;
    }

    private void sendMoveStudentsRequest() {
        Action movestud = new Action();
        movestud.setGamePhase(GamePhase.MOVE_3_STUDENTS);
        Color[] student = new Color[nof_players + 1];
        int[] island_indexes = new int[student.length];
        Places[] destinations = new Places[student.length];
        for(int i = 0; i < student.length; i++){
            student[i] = (Color) move_students.get(i).getColor().getProperties().get("color");
            island_indexes[i] = move_students.get(i).getIsland_index();
            destinations[i] = island_indexes[i] > 12 ? Places.DINING_HALL : Places.ISLAND;
        }
        movestud.setThreeStudents(student);
        movestud.setThreeStudentPlaces(destinations);
        movestud.setIslandIndexes(island_indexes);
        try {
            msg.send(movestud);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        last_sent = movestud;
    }

    private void sendMotherNatureRequest(int index){
        Action movemother = new Action();
        movemother.setGamePhase(GamePhase.MOVE_MOTHERNATURE);
        //calculate the increment
        int increment = index - model.getMotherNature().getIslandIndex();
        if(increment < 0) increment = GameBoard.NOF_ISLAND + increment;
        movemother.setMothernatureIncrement(increment);
        try {
            msg.send(movemother);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        last_sent = movemother;
    }

    private void sendDrainCloudRequest(int index){
        Action draincloud = new Action();
        draincloud.setGamePhase(GamePhase.DRAIN_CLOUD);
        draincloud.setCloudIndex(index);
        try {
            msg.send(draincloud);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        last_sent = draincloud;
    }

    private void sendPutOnCloudsRequest(){
        Action putonclouds = new Action();
        putonclouds.setGamePhase(GamePhase.PUT_ON_CLOUDS);

        try {
            msg.send(putonclouds);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        last_sent = putonclouds;
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
        double[] start = new double[2];
        pane.setOnMousePressed(e -> {
            start[0] = pane.getTranslateX() - e.getSceneX();
            start[1] = pane.getTranslateY() - e.getSceneY();
        });
        pane.setOnMouseDragged(e -> {
            pane.setTranslateX(e.getSceneX() + start[0]);
            pane.setTranslateY(e.getSceneY() + start[1]);
        });
    }

    private void disableGUI(){
        msg.disable(); //blocks gui's traffic on the network
        /*
        Disabilita:
        - bottone character card
        - bottone assist card
        -
         */
    }

    private void enableGUI(){
        msg.enable(); //allows the gui to communicate through the network
    }

    private void setIslandPos(Group island, double x, double y){
        for(Node child : island.getChildren()){
            if(child instanceof Group){
                setIslandPos((Group) child, x, y);
            } else {
                if(child instanceof GridPane){
                    int divisor = child.getProperties().get("type") != null ? 5 : 6;
                    x += ISLAND_SIZE / divisor;
                    y += ISLAND_SIZE / divisor;
                }/* else if(child.getProperties().get("mothernature")!=null){
                    x -= ISLAND_SIZE / 5;
                    y -= ISLAND_SIZE / 5;
                }*/
                TranslateTransition translate = new TranslateTransition();
                translate.setDuration(Duration.millis(1000));
                translate.setFromX(child.getTranslateX());
                translate.setFromY(child.getTranslateY());
                translate.setToX(x);
                translate.setToY(y);
                translate.setNode(child);
                translate.play();
                if(child.getProperties().get("type")!=null && child.getProperties().get("type").equals("island")){
                    int index = (int) child.getProperties().get("index");
                    if(model.getMotherNature().getIslandIndex() == index){
                        TranslateTransition tt2 = new TranslateTransition();
                        tt2.setNode(mothernature_img);
                        tt2.setFromX(mothernature_img.getTranslateX());
                        tt2.setFromY(mothernature_img.getTranslateY());
                        tt2.setToX(x);
                        tt2.setToY(y);
                        tt2.play();
                    }
                }
            }
        }
    }

    private void translateMotherNature(){
        /*Node island_elem = islands[model.getMotherNature().getIslandIndex()].getChildren().get(0);
        System.out.println("Translating mother nature");
        TranslateTransition translate = new TranslateTransition();
        translate.setDuration(Duration.millis(1000));
        translate.setFromX(mothernature_img.getTranslateX());
        translate.setFromY(mothernature_img.getTranslateY());
        translate.setToX((Double) island_elem.getProperties().get("new-x"));
        translate.setToY((Double) island_elem.getProperties().get("new-y"));
        System.out.println((Double) island_elem.getProperties().get("new-x") + ", " + (Double) island_elem.getProperties().get("new-y"));
        translate.setNode(mothernature_img);
        translate.play();*/
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

    private void fillIslands(int offset){ //need to subtract this offset
        Island[] model_islands = model.getIslands();
        for(int i = 0; i < model_islands.length; i++){
            GridPane island_grid = (GridPane) islands[i].getChildren().get(1);
            island_grid.getChildren().clear();
            int new_index = i - offset;
            if(new_index < 0){
                new_index += 12;
            }
            Map<Color, Integer> island_studs = model_islands[new_index].getStudents();
            int total = GenericUtils.sumValues(island_studs) + (model_islands[new_index].hasTower() ? 1 : 0);
            if(total == 0) total++;
            int count = 0;
            int nof_col = (int) Math.ceil(total * 0.5);
            if(nof_col == 0) nof_col++;
            int divisor = Math.max(nof_col, total / nof_col);
            for(Color color : island_studs.keySet()) {
                for (int o = 0; o < island_studs.getOrDefault(color, 0); o++) {
                    ImageView student = new ImageView(
                            new Image(String.valueOf(getClass().getResource("/Students/" + Color.colorToString(color) + "stud.png")))
                    );
                    student.setFitHeight(ISLAND_SIZE * 0.6 / divisor);
                    student.setFitWidth(ISLAND_SIZE * 0.6 / divisor);
                    island_grid.add(student, count % nof_col, count / nof_col);
                    count++;
                }
            }
            if(model_islands[new_index].hasTower()){
                ImageView tower = getTowerImage(model_islands[new_index].getTowerColor());
                tower.setFitHeight(ISLAND_SIZE /  divisor * 0.65);
                tower.setFitWidth(ISLAND_SIZE / divisor * 0.8);
                island_grid.add(tower, count % nof_col, count / nof_col);
            }
        }
        translateMotherNature();
    }

    private void alignIslands(int[] islands_groups){
        /*
        problema quando l'ultima cella di islands_groups è maggiore di 1
        perché devo collegare l'isola 12 all'isola 1
        ma l'algoritmo allinea male (la 11 alla 12)
         */
        //Determino quale sia la prima isola da cui partire
        int new_start = (13 - islands_groups[islands_groups.length - 1]) % 12;
        System.out.println("Islands groups:");
        for(int i = 0; i < islands_groups.length; i++){
            System.out.println(i + ") " + islands_groups[i]);
        }
        System.out.println("Islands:");
        for(int i = 0; i < model.getIslands().length; i++){
            System.out.println(i + ") " + model.getIslands()[i].hasNext());
        }
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
                count++;
            }
            angle += 2 * Math.PI / islands_groups.length;
        }
        fillIslands(new_start);
    }
}
