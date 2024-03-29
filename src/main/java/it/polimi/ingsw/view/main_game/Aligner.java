package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.entities.cards.CharacterCard;
import it.polimi.ingsw.model.entities.cards.LockBehavior;
import it.polimi.ingsw.model.entities.cards.StudentBehavior;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.places.School;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;
import it.polimi.ingsw.view.Positions;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Aligner {
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;
    private Handler handler;
    private Deliverer deliverer;
    private boolean disabled;
    private GameBoard model;
    private Rectangle dining_area;
    private Label[] island_indexes;
    private ImageView[] island_icons;
    private Group[] islands;

    public Aligner(Handler handler, Deliverer deliverer){
        this.handler = handler;
        this.deliverer = deliverer;
        island_indexes = new Label[12];
        island_icons = new ImageView[12];
        islands = new Group[12];
        for(int i = 0; i < island_indexes.length; i++){
            island_indexes[i] = getWhiteLabel(" " + (i+1)+ " ");
            island_indexes[i].setFont(new Font(30)); // set to Label
            island_indexes[i].setBackground((new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(0, 0, 0), new CornerRadii(0), new Insets(0)))));
            island_indexes[i].setTranslateY(ISLAND_SIZE * 0.4);
        }
    }

    public void disable(){
        disabled = true;
    }

    public void enable(){
        disabled = false;
    }

    public void setGameBoard(GameBoard model){
        this.model = model;
    }

    public void createIslandGroup(Group islands_container, Group[] islands, TitledPane moving_studs_container){
        this.islands = islands;
        for(int i = 0; i < islands.length; i++){
            islands[i] = new Group();
            island_icons[i]  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Islands/island" + i % 2 + ".png")))
            );
            island_icons[i].setFitHeight(ISLAND_SIZE);
            island_icons[i].setFitWidth(ISLAND_SIZE);
            island_icons[i].getProperties().put("type", "island");
            island_icons[i].getProperties().put("index", i);
            GridPane students = new GridPane();
            students.getProperties().put("type", "island-students");
            students.setHgap(1);
            students.setVgap(2);
            Label index = island_indexes[i];
            StackPane island_icon_indexed = new StackPane(island_icons[i], index);
            ImageView lock = deliverer.getLockImage();
            lock.setFitHeight(ISLAND_SIZE /  2);
            lock.setFitWidth(ISLAND_SIZE / 2);
            lock.setVisible(false);
            this.islands[i].getChildren().addAll(island_icon_indexed, students, deliverer.getMotherNatureImage(), lock);
            islands_container.getChildren().add(islands[i]);
            //Mother nature menu
            ContextMenu mn_menu = new ContextMenu();
            MenuItem request_mn_item = new MenuItem("Move mother nature here");
            int finalI = i;
            request_mn_item.setOnAction(e -> {
                handler.sendMotherNatureRequest(this.islands[finalI], model);
            });
            mn_menu.getItems().add(request_mn_item);
            students.setOnContextMenuRequested(e -> mn_menu.show(island_icons[finalI], e.getScreenX(), e.getScreenY()));
            //adding mouse management to island
            this.islands[i].getProperties().put("type", "island");
            this.islands[i].getProperties().put("index", i);
            this.islands[i].setOnMouseClicked(e -> {
                if(!disabled) {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        handler.selectMovingSubject(this.islands[finalI], model.getPlayers().length, moving_studs_container, dining_area);
                    } else {
                        mn_menu.show(this.islands[finalI], e.getScreenX(), e.getScreenY());
                    }
                } else {
                    handler.showWrongTurnPopUp();
                }
            });
        }
    }

    public void createCloudsGroup(Group clouds_container, Group[] clouds) {
        for(int i = 0; i < clouds.length; i++){
            clouds[i] = new Group();
            ImageView cloud_icon  = new ImageView(
                    new Image(String.valueOf(getClass().getResource("/Clouds/cloud" + (clouds.length-2) + ".png")))
            );
            cloud_icon.setEffect(new DropShadow());
            cloud_icon.setFitHeight(ISLAND_SIZE);
            cloud_icon.setFitWidth(ISLAND_SIZE);
            GridPane students = new GridPane();
            students.setHgap(2);
            students.setVgap(2);
            clouds[i].getChildren().addAll(cloud_icon, students);
            clouds[i].setOnMouseClicked(e -> {
                if(disabled) {
                    handler.showWrongTurnPopUp();
                }
            });
            clouds_container.getChildren().add(clouds[i]);
            //Drain cloud menu
            ContextMenu dc_menu = new ContextMenu();
            MenuItem request_dc_item = new MenuItem("Retrieve these students");
            int finalI = i;
            request_dc_item.setOnAction(e -> {
                handler.sendDrainCloudRequest(finalI);
            });
            dc_menu.getItems().add(request_dc_item);
            cloud_icon.setOnContextMenuRequested(e -> dc_menu.show(cloud_icon, e.getScreenX(), e.getScreenY()));
            students.setOnContextMenuRequested(e -> dc_menu.show(students, e.getScreenX(), e.getScreenY()));
        }
    }

    public void createRoot(SubScene subscene, Group islands_container, Group clouds_container) {
        Image water_img = new Image(String.valueOf(getClass().getResource("/Islands/water.gif")));
        GridPane bg = new GridPane();
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++){
                ImageView water = new ImageView(water_img);
                water.setPreserveRatio(true);
                water.setFitHeight(subscene.getHeight() / 3);
                bg.add(water, row, col);
            }
        }
        //bg.setTranslateX(-1100);
        //bg.setTranslateY(-900);
        Pane pane = new Pane(islands_container, clouds_container);
        pane.setTranslateX(-pane.getWidth() / 2);
        pane.setTranslateY(-pane.getHeight() / 2);
        StackPane pane_with_bg = new StackPane(bg, pane);
        ScrollPane sp = new ScrollPane(pane_with_bg);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        BorderPane bp = new BorderPane(sp);
        subscene.setRoot(bp);
        subscene.setFill(Paint.valueOf("#ff0000"));//new Background(new BackgroundFill(javafx.scene.paint.Color.web("#00FFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        addZoomListener(pane);
        forceZoom(pane, 300, true);
        applyNavigationListener(pane);
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
        double scalex = pane.getScaleX() * zoomFactor;
        double scaley = pane.getScaleY() * zoomFactor;
        if(!(scalex < 0.28 || scaley < 0.28 || scalex > 0.7 || scaley > 0.7)){
            pane.setScaleX(pane.getScaleX() * zoomFactor);
            pane.setScaleY(pane.getScaleY() * zoomFactor);
        }
    }

    private void applyNavigationListener(Pane pane){
        double[] start = new double[2];
        pane.setOnMousePressed(e -> {
            start[0] = pane.getTranslateX() - e.getSceneX();
            start[1] = pane.getTranslateY() - e.getSceneY();
        });
        pane.setOnMouseDragged(e -> {
            double translatex = e.getSceneX() + start[0];
            double translatey = e.getSceneY() + start[1];
            if(!(translatex > 350 || translatex < -350)) {
                pane.setTranslateX(e.getSceneX() + start[0]);
            }
           if(!(translatey > 350 || translatey < -350)) {
                pane.setTranslateY(e.getSceneY() + start[1]);
            }
        });
    }

    public static Label getWhiteLabel(String text){
        Label l = new Label(text);
        l.setTextFill(Paint.valueOf("#ffffff"));
        return l;
    }

    public static Label getBlackLabel(String text){
        Label l = new Label(text);
        l.setTextFill(Paint.valueOf("#000000"));
        return l;
    }

    public void copyOtherSchools(Pane other_schools_container, String username){
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
            for(int t = 0; t < tot_tower; t++){
                ImageView tower_img = deliverer.getPlacedTowerImage(others[i].getColor());
                if(t >= others[i].getNumberOfUnplacedTowers()){
                    handler.applyNotPresentEffect(tower_img);
                } else tower_img.setEffect(new DropShadow());
                towers.getChildren().add(tower_img);
            }
            others_container[i].getChildren().add(towers);
            //Professors
            others_container[i].getChildren().add(getWhiteLabel("Professors Room"));
            HBox professors = new HBox();
            for(int t = 0; t < model.getProfessors().length; t++){
                Color prof_col = model.getProfessors()[t].getColor();
                ImageView prof_img = deliverer.getProfessorImage(prof_col);
                Player detained = model.getProfessors()[t].getPlayer();
                if(detained == null || !detained.equals(others[i])) {
                    handler.applyNotPresentEffect(prof_img);
                    prof_img.setOpacity(0.5);
                }
                else prof_img.setEffect(new DropShadow());
                professors.getChildren().add(prof_img);
            }
            others_container[i].getChildren().add(professors);
            //Dining hall
            others_container[i].getChildren().add(getWhiteLabel("Dining Hall"));
            Map<Color, Integer> dining = others[i].getDiningStudents();
            for(Color col : Color.getStudentColors()) {
                HBox diningbox = new HBox();
                if(dining.getOrDefault(col, 0) == 0){
                    ImageView empty_stud = deliverer.getStudentImage(col);
                    handler.applyNotPresentEffect(empty_stud);
                    empty_stud.setOpacity(0);
                    diningbox.getChildren().add(empty_stud);
                } else {
                    for (int t = 0; t < dining.getOrDefault(col, 0); t++) {
                        ImageView din_img = deliverer.getStudentImage(col);
                        din_img.setEffect(new DropShadow());
                        diningbox.getChildren().add(din_img);
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
                    ImageView ent_img = deliverer.getStudentImage(col);
                    ent_img.setEffect(new DropShadow());
                    entrancebox.getChildren().add(ent_img);
                }
            }
            others_container[i].getChildren().add(entrancebox);
            //Coins
            others_container[i].getChildren().add(getWhiteLabel("Coins"));
            int coins = others[i].getCoins();
            HBox coins_box = new HBox();
            for(int c = 0; c < coins; c++){
                ImageView coin_img = deliverer.getCoinImage();
                coin_img.setEffect(new DropShadow());
                coins_box.getChildren().add(coin_img);
                if(c>=10) break;
            }
            others_container[i].getChildren().add(coins_box);
            //Final
            grid.add(others_container[i], i, 0);
        }
        grid.setGridLinesVisible(true);
        grid.setHgap(3);
        grid.setVgap(3);
        other_schools_container.getChildren().add(grid);
    }

    public void drawTowers(Group school_elements, School school){
        Color tower_color = school.getTowerColor();
        int xoff = 0, yoff = 0;
        for(int i = 0; i < school.getNumberOfTowers(); i++){
            ImageView tower = deliverer.getPlacedTowerImage(tower_color);
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

    public Group drawProfessors(Color[] sorted_stud_colors, String username){
        Group school_elements = new Group();
        for(int i = 0; i < sorted_stud_colors.length; i++){
            Player reference = model.getProfFromColor(sorted_stud_colors[i]).getPlayer();
            if(reference != null && reference.getUsername().equals(username)) {
                ImageView prof = deliverer.getProfessorImage(sorted_stud_colors[i]);
                prof.getProperties().put("type", "prof");
                prof.setTranslateX(Positions.PROFESSORS.getX() + i * Positions.PROFESSORS.getXOff());
                prof.setTranslateY(Positions.PROFESSORS.getY() + i * Positions.PROFESSORS.getYOff());
                school_elements.getChildren().add(prof);
            }
        }
        return school_elements;
    }

    public void drawDiningHall(Group school_elements, School school, Color[] sorted_stud_colors, int nof_players, TitledPane moving_studs_container) {
        float off = Positions.DINING_HALL_STUDENTS.getXOff();
        float[] xoff = {0, off, 2*off, 3*off, 4*off};
        float[] yoff = new float[5];
        int index_col = 0;
        Map<Color, Integer> students = school.getDiningStudents();
        for(Color color : sorted_stud_colors){ //for each color
            for(int i = 0; i < students.getOrDefault(color, 0); i++){ //as many as the nof students of that color
                ImageView student = deliverer.getStudentImage(color);
                student.setTranslateX(Positions.DINING_HALL_STUDENTS.getX() + xoff[index_col]);
                student.setTranslateY(Positions.DINING_HALL_STUDENTS.getY() + yoff[index_col]);
                school_elements.getChildren().add(student);
                yoff[index_col] += Positions.DINING_HALL_STUDENTS.getYOff();
            }
            index_col++;
        }
        dining_area = new Rectangle();
        dining_area.setOpacity(0);
        dining_area.setFill(new ImagePattern(deliverer.getClickImage()));
        dining_area.setX(223);
        dining_area.setY(250);
        dining_area.setWidth(200);
        dining_area.setHeight(290);
        ImageView iv = new ImageView();
        iv.getProperties().put("type", "dining");
        dining_area.setOnMouseClicked(e -> {if(!disabled) handler.selectMovingSubject(iv, nof_players, moving_studs_container, dining_area); else handler.showWrongTurnPopUp();});
        school_elements.getChildren().add(dining_area);
    }

    public void drawEntrance(Group school_elements, School school, int nof_players, TitledPane moving_studs_container){
        int xoff = 0, yoff = 0, count = 0;
        Map<Color, Integer> students = school.getEntranceStudents();
        for(Color col : students.keySet()){
            for(int i = 0; i < students.getOrDefault(col, 0); i++){
                ImageView student = new ImageView(
                        new Image(String.valueOf(getClass().getResource("/Students/"+Color.colorToString(col)+"stud.png")))
                );
                student.setFitWidth(Positions.ENTRANCE_STUDENTS.getWidth());
                student.setFitHeight(Positions.ENTRANCE_STUDENTS.getHeight());
                student.setEffect(null);
                student.setTranslateX(Positions.ENTRANCE_STUDENTS.getX() + xoff * Positions.ENTRANCE_STUDENTS.getXOff());
                student.setTranslateY(Positions.ENTRANCE_STUDENTS.getY() + yoff * Positions.ENTRANCE_STUDENTS.getYOff());
                student.setOnMouseClicked(e -> {if(!disabled) handler.selectMovingSubject(student, nof_players, moving_studs_container, dining_area); else handler.showWrongTurnPopUp();});
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

    public void copyMotherNature(Group[] islands) {
        int new_start = 0;
        boolean found = false;
        for(int i = 0; i < model.getIslands().length && !found; i++){
            try {
                if(!model.getIslands()[i].hasPrevious()){
                    new_start = i;
                    found = true;
                }
            } catch (EriantysException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < model.getIslands().length; i++){
            int new_index = i - new_start;
            if(new_index < 0){
                new_index = 12 + new_index;
            } else if(new_index >= 12){
                new_index %= 12;
            }
            boolean visibility = i == model.getMotherNature().getIslandIndex();
            for(Node n : islands[new_index].getChildren()){
                if(n instanceof ImageView && n.getProperties().get("mothernature")!=null)
                    n.setVisible(visibility);
            }
        }
    }

    public void copyCharacterCards(ChoiceBox<String> cc_values, ImageView[] char_card, GridPane[] cc_content, GridPane[] cc_coins, VBox[] vboxes){
        //Setting the correct images
        cc_values.getItems().clear();
        for(GridPane gp : cc_coins)
            gp.getChildren().clear();
        for(GridPane gp : cc_content)
            gp.getChildren().clear();
        for(VBox vb : vboxes)
            vb.getChildren().clear();
        for(int i = 0; i < 3; i++){
            try {
                CharacterCard ToRep = model.getActiveCharacterCard(i);
                //Setting choice
                cc_values.getItems().add(ToRep.getName() + " (" + GenericUtils.getOrdinal(i+1) + ")");
                //Setting image
                char_card[i].setImage(deliverer.getCharacterCardImage(ToRep.getID()));
                Tooltip description = new Tooltip(ToRep.getDescription().replace("\t", ""));
                Tooltip.install(char_card[i], description);
                Tooltip.install(cc_coins[i], description);
                Tooltip.install(cc_content[i], description);
                //Centering
                //Setting content
                int row = 0, colm = 0;
                int count = 0;
                if(ToRep.getBehavior() instanceof StudentBehavior){
                    Color[] students = ToRep.getBehavior().getAvailableStudents();
                    for(Color col : students){
                        StackPane sp_stud = new StackPane();
                        sp_stud.getChildren().addAll(deliverer.getStudentImage(col), getBlackLabel((count+1)+""));
                        count++;
                        cc_content[i].add(sp_stud, row, colm);
                        row++;
                        if(row == 2){ colm++; row = 0;}
                    }
                } else if(ToRep.getBehavior() instanceof LockBehavior){
                    row = 0; colm = 0;
                    for(int l = 0; l < ToRep.getBehavior().getAvailableLocks(); l++){
                        cc_content[i].add(deliverer.getLockImage(), row, colm);
                        row++;
                        if(row == 2){ colm++; row = 0;}
                    }
                }
                //Setting coins
                int coins_to_place = ToRep.getPrice() - (ToRep.getID()%3+1);
                row = 0; colm = 0;
                for(int c = 0; c < coins_to_place; c++){
                    cc_coins[i].add(deliverer.getCoinImage(), row, colm);
                    row++;
                    if(row == 4){ colm++; row = 0;}
                }
                //Setting form
                addForm(ToRep.getID(), vboxes[i]);
            } catch (EriantysException e) {
                e.printStackTrace();
            }
        }
        cc_values.setValue(cc_values.getItems().get(0));
    }

    public void copyClouds(int nof_players, Group[] clouds, SubScene subscene, ImageView mothernature_img){
        alignClouds(clouds, subscene, mothernature_img);
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

    public void copyIslands(SubScene subscene, Group[] islands, ImageView mothernature_img) throws EriantysException {
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
        alignIslands(island_groups, subscene, islands, mothernature_img);
    }

    public void copySchool(School school, boolean mine, AnchorPane player_container, String username, TitledPane moving_studs_container){
        AnchorPane pane = mine ? player_container : null;
        //removes each non-imageview child
        if(pane.getChildren().size() > 2) pane.getChildren().remove(2);
        Group school_elements = new Group();
        //Towers
        drawTowers(school_elements, school);
        //Colors in the correct order
        Color[] sorted_stud_colors = {Color.GREEN, Color.RED, Color.YELLOW, Color.PINK, Color.BLUE};
        //Professors
        Group prof_container = drawProfessors(sorted_stud_colors, username);
        //Students
        drawDiningHall(school_elements, school, sorted_stud_colors, model.getPlayers().length, moving_studs_container);
        drawEntrance(school_elements, school, model.getPlayers().length, moving_studs_container);
        pane.getChildren().addAll(school_elements, prof_container);
        int coins = model.getPlayerByUsername(username).getCoins();
        VBox coin_container = new VBox();
        for(int i = 0; i < coins; i++){
            ImageView coin_img = deliverer.getCoinImage();
            coin_img.setEffect(new DropShadow());
            coin_container.getChildren().addAll(coin_img);
            if(i >= 17){
                break;
            }
        }
        coin_container.setTranslateX(160);
        coin_container.setTranslateY(110);
        pane.getChildren().add(coin_container);
    }

    public void copyAssistCards(ImageView[] crosses, ImageView[] assistants, String username) { //TODO: FIX blue
        boolean[] available_assist_cards = new boolean[crosses.length];
        AssistCard[] player_cards = model.getPlayerByUsername(username).getWizard().getCards();
        int remaining = crosses.length;
        for(int i = 0; i < player_cards.length; i++){
            if(player_cards[i].isPlayed()){
                available_assist_cards[i] = false; //the ones the player has already played
                remaining--;
                handler.setGrayScale(assistants[i]);
                handler.removeSelectedEffect(crosses[i]);
                crosses[i].setVisible(true);
            } else {
                available_assist_cards[i] = true;
                crosses[i].setVisible(false);
                handler.removeSelectedEffect(assistants[i]);
                assistants[i].setEffect(new DropShadow());
            }
        }
        //now we calculate the ones that are played in this turn by the other players
        for(Player player : model.getPlayers()){
            if(player.getUsername().equals(username)) continue;
            if(player.getLastPlayedCard()!=null){
                int assist_card_id = player.getLastPlayedCard().getValue() - 1;
                if(remaining > 1){
                    available_assist_cards[assist_card_id] = false;
                    handler.setGrayScale(assistants[assist_card_id]);
                    handler.setTint(crosses[assist_card_id], javafx.scene.paint.Color.web("#4756ba"));
                    crosses[assist_card_id].setVisible(true);
                    remaining--;
                }
            }
        }
    }

    private void setIslandPos(Group island, double x, double y, ImageView mothernature_img){
        for(Node child : island.getChildren()){
            if(child instanceof Group){
                setIslandPos((Group) child, x, y, mothernature_img);
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
            }
        }
    }

    private void alignClouds(Group[] clouds, SubScene subscene, ImageView mothernature_img){
        float angle = 0;
        for (Group cloud : clouds) {
            double x = subscene.getWidth() / 2 + Math.cos(angle) * ISLAND_SIZE - ISLAND_SIZE / 2;
            double y = subscene.getHeight() / 2 + Math.sin(angle) * ISLAND_SIZE - ISLAND_SIZE / 2;
            setIslandPos(cloud, x, y, mothernature_img);
            angle += 2 * Math.PI / clouds.length;
        }
    }

    private void fillIslands(int offset, Group[] islands){ //need to subtract this offset
        Island[] model_islands = model.getIslands();
        for(int i = 0; i < model_islands.length; i++){
            GridPane island_grid = (GridPane) islands[i].getChildren().get(1);
            island_grid.getChildren().clear();
            int new_index = i + offset;
            new_index %= 12;
            //editing label corresponding to new index
            island_indexes[i].setText(" " + (new_index+1)+ " ");
            this.islands[i].getProperties().put("index", new_index);
            this.islands[i].setEffect(null);
            this.islands[i].setEffect(new DropShadow());
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
                ImageView tower = deliverer.getPlacedTowerImage(model_islands[new_index].getTowerColor());
                tower.setFitHeight(ISLAND_SIZE /  divisor * 0.65);
                tower.setFitWidth(ISLAND_SIZE / divisor * 0.8);
                island_grid.add(tower, count % nof_col, count / nof_col);
            }
            this.islands[i].getChildren().get(3).setVisible(model_islands[new_index].isLocked()); //lock
        }
    }

    private void alignIslands(int[] islands_groups, SubScene subscene, Group[] islands, ImageView mothernature_img) throws EriantysException {
        //new start is the first island without a previous
        int new_start = 0;
        boolean found = false;
        for(int i = 0; i < model.getIslands().length && !found; i++){
            if(!model.getIslands()[i].hasPrevious()){
                new_start = i;
                found = true;
            }
        }
        float angle = 0;
        int count = 0;
        int island_number = 0;
        for (int islands_group : islands_groups) {
            int xoff = 0;
            int yoff = 0;
            island_number += islands_group;
            for (int o = 0; o < islands_group; o++) {
                if (o % 4 == 0 && o > 0) {
                    //int direction = (island_number >= 0 && island_number <=4) || (island_number >8 && island_number < 12) ? -1 : 1;
                    yoff += ISLAND_SIZE * 0.95;
                    xoff = 0;
                } else if (o > 0) {
                    int direction = island_number % 2 == 0 ? 1 : -1;
                    xoff += ISLAND_SIZE * 0.75 * direction;
                }
                //resetIslandPos(islands[count]);
                int temp_yoff = 0;
                if (o % 2 == 1) {
                    temp_yoff += ISLAND_SIZE * 0.475;
                }
                double x = subscene.getWidth() / 2 + Math.cos(angle) * ISLAND_SIZE * 4.4 + xoff - ISLAND_SIZE / 2;
                double y = subscene.getHeight() / 2 + Math.sin(angle) * ISLAND_SIZE * 4.4 + yoff - ISLAND_SIZE / 2 + temp_yoff;
                islands[count].setEffect(null);
                setIslandPos(islands[count], x, y, mothernature_img);
                count++;
            }
            angle += 2 * Math.PI / islands_groups.length;
        }
        fillIslands(new_start, islands);
    }

    //TODO: check that each cc has the correct behavior (#7 hasn't)
    private void addForm(int id, VBox vbox) {
        switch(id){
            case 0:
                //1 island index array
                vbox.getChildren().add(getWhiteLabel("Destination island"));
                vbox.getChildren().add(getIntegerChooser("island-0", 1, 12));
                //1 student index array
                vbox.getChildren().add(getWhiteLabel("This card's students"));
                vbox.getChildren().add(getIntegerChooser("studentindex-0", 1, 4));
                break;
            case 1:
                break;
            case 2:
                vbox.getChildren().add(getWhiteLabel("Chosen island"));
                vbox.getChildren().add(getIntegerChooser("island-2", 1, 12));
                break;
            case 3:
                break;
            case 4:
                vbox.getChildren().add(getWhiteLabel("Island to lock"));
                vbox.getChildren().add(getIntegerChooser("island-4", 1, 12));
                break;
            case 5:
                break;
            case 6:
                //up to 3 students
                vbox.getChildren().add(getWhiteLabel("Your entrance students"));
                HBox card6_hbox = new HBox();
                card6_hbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-6-0"));
                card6_hbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-6-1"));
                card6_hbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-6-2"));
                vbox.getChildren().add(card6_hbox);
                vbox.getChildren().add(getWhiteLabel("This card's students"));
                HBox card6_hbox_2 = new HBox();
                card6_hbox_2.getChildren().add(getIntegerChooser("studentindex-6-0", 1, 6));
                card6_hbox_2.getChildren().add(getIntegerChooser("studentindex-6-1", 1, 6));
                card6_hbox_2.getChildren().add(getIntegerChooser("studentindex-6-2", 1, 6));
                vbox.getChildren().add(card6_hbox_2);
                break;
            case 7:
                break;
            case 8:
                vbox.getChildren().add(getWhiteLabel("Chosen color"));
                vbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-8"));
                break;
            case 9:
                //up to 2 students
                vbox.getChildren().add(getWhiteLabel("Your entrance students"));
                HBox card9_hbox = new HBox();
                card9_hbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-9-0"));
                card9_hbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-9-1"));
                vbox.getChildren().add(card9_hbox);
                vbox.getChildren().add(getWhiteLabel("Your Dining hall students"));
                HBox card9_hbox_2 = new HBox();
                card9_hbox_2.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-9b-0"));
                card9_hbox_2.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-9b-1"));
                vbox.getChildren().add(card9_hbox_2);
                break;
            case 10:
                vbox.getChildren().add(getWhiteLabel("This card's students"));
                vbox.getChildren().add(getIntegerChooser("studentindex-10", 1, 4));
                break;
            case 11:
                vbox.getChildren().add(getWhiteLabel("Color to drop off"));
                vbox.getChildren().add(getColorChooser(Color.getStudentColors(), "studentcolor-11"));
                break;
        }
    }

    private ChoiceBox<Integer> getIntegerChooser(String id, int min, int max){
        ChoiceBox<Integer> chooser = new ChoiceBox<>();
        chooser.setId(id); //scene.lookup
        chooser.getStyleClass().add("choiceBox");
        for(int i = min; i <= max; i++){
            chooser.getItems().add(i);
        }
        return chooser;
    }

    private ChoiceBox<Color> getColorChooser(Color[] colors, String id){
        ChoiceBox<Color> chooser = new ChoiceBox<>();
        chooser.setId(id); //scene.lookup
        chooser.getStyleClass().add("choiceBox");
        for(Color col : colors){
            chooser.getItems().add(col);
        }
        return chooser;
    }
}
