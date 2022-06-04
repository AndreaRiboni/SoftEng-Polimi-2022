package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.GamePhase;
import it.polimi.ingsw.model.utils.packets.StudentLocation;
import it.polimi.ingsw.view.Positions;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deliverer {
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;
    private Map<String, Image> cache;

    public Deliverer(){
        cache = new HashMap<>();
    }

    private void addToCache(String path, Image img){
        if(!cache.containsKey(path))
            cache.put(path, img);
    }

    private Image getFromCache(String path){
        if(cache.containsKey(path))
            return cache.get(path);
        else {
            Image img = new Image(path);
            addToCache(path, img);
            return img;
        }
    }

    public ImageView getPlacedTowerImage(Color tower_color){
        String path = String.valueOf(getClass().getResource("/Towers/"+Color.colorToString(tower_color)+"_tower.png"));
        ImageView tower = new ImageView(
                getFromCache(path)
        );
        tower.setFitWidth(Positions.TOWERS.getWidth());
        tower.setFitHeight(Positions.TOWERS.getHeight());
        return tower;
    }

    public ImageView getMotherNatureImage(){
        String path = String.valueOf(getClass().getResource("/MotherNature/mother_nature.gif"));
        ImageView mn = new ImageView(
                getFromCache(path)
        );
        mn.setFitWidth(ISLAND_SIZE);
        mn.setFitHeight(ISLAND_SIZE);
        mn.getProperties().put("mothernature", true);
        mn.setEffect(new Glow(0.5));
        return mn;
    }

    public ImageView getProfessorImage(Color prof_color){
        String path = String.valueOf(getClass().getResource("/Professors/" + Color.colorToString(prof_color) + "prof.png"));
        ImageView prof = new ImageView(
                getFromCache(path)
        );
        prof.setFitWidth(Positions.PROFESSORS.getWidth());
        prof.setFitHeight(Positions.PROFESSORS.getHeight());
        return prof;
    }

    public ImageView getStudentImage(Color color){
        String path = String.valueOf(getClass().getResource("/Students/"+Color.colorToString(color)+"stud.png"));
        ImageView student = new ImageView(
                getFromCache(path)
        );
        student.setFitWidth(Positions.DINING_HALL_STUDENTS.getWidth());
        student.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return student;
    }

    public Image getCharacterCardImage(int id){
        String path = String.valueOf(getClass().getResource("/characters/character_" + (id+1) + ".jpg"));
        //why the hell does character_7 returns character_8
        return new Image(path);
    }

    public ImageView getLockImage(){
        String path = String.valueOf(getClass().getResource("/characters/lock.png"));
        ImageView lock = new ImageView(
                getFromCache(path)
        );
        lock.setPreserveRatio(true);
        lock.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return lock;
    }

    public ImageView getCoinImage(){
        String path = String.valueOf(getClass().getResource("/characters/coin.png"));
        ImageView coin = new ImageView(
                getFromCache(path)
        );
        coin.setPreserveRatio(true);
        coin.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return coin;
    }

    public Group getIslandIcon(int island_index){
        String path = String.valueOf(getClass().getResource("/graphics/island.png"));
        ImageView island_img = new ImageView(
                getFromCache(path)
        );
        island_img.setPreserveRatio(true);
        island_img.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight() * 2);
        Label island_text = Aligner.getWhiteLabel(""+(island_index+1));
        island_text.setFont(Font.font("Arial", FontWeight.BLACK, 20));
        StackPane sp = new StackPane(island_img, island_text);
        return new Group(sp);
    }

    public ImageView getArrowImage(){
        String path = String.valueOf(getClass().getResource("/graphics/arrow.png"));
        ImageView arrow = new ImageView(
                getFromCache(path)
        );
        arrow.setPreserveRatio(true);
        arrow.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight() * 0.75);
        return arrow;
    }

    public ImageView getDiningHallImage(){
        String path = String.valueOf(getClass().getResource("/graphics/dining_hall.png"));
        ImageView dining = new ImageView(
                getFromCache(path)
        );
        dining.setPreserveRatio(true);
        dining.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight() * 2);
        return dining;
    }

    public VBox composeMovingStudent(List<StudentLocation> sl_list){
        VBox vbox = new VBox();
        for(StudentLocation sl : sl_list){
            Object stud = sl.getColor();
            int location = sl.getIsland_index();
            if(stud == null || location == -1) return vbox;
            Color col = (Color) ((ImageView)stud).getProperties().get("color");
            HBox sl_hbox = new HBox();
            sl_hbox.getChildren().addAll(
                    getStudentImage(col),
                    getArrowImage(),
                    location == 100 ? getDiningHallImage() : getIslandIcon(location)
            );
            sl_hbox.setAlignment(Pos.CENTER_LEFT);
            vbox.getChildren().add(sl_hbox);
        }
        if(sl_list.isEmpty()){
            vbox.getChildren().add(Aligner.getBlackLabel("You haven't moved any student"));
        }
        return vbox;
    }

    public Image getClickImage(){
        String path = String.valueOf(getClass().getResource("/graphics/stars.png"));
        return getFromCache(path);
    }

    public HBox getGamephases(List<GamePhase> gp_list){
        String filename = "";
        HBox gp_container = new HBox();
        for(GamePhase gp : gp_list){
            switch(gp){
                case MOVE_3_STUDENTS:
                    filename = "move_students";
                    break;
                case MOVE_MOTHERNATURE:
                    filename = "move_mother_nature";
                    break;
                case USE_CHARACTER_CARD:
                    filename = "use_cc";
                    break;
                case DRAW_ASSIST_CARD:
                    filename = "draw_assist_card";
                    break;
                case DRAIN_CLOUD:
                    filename = "drain_cloud";
                    break;
            }
            if(!filename.isEmpty()){
                ImageView gp_viewer = new ImageView(
                        getFromCache(String.valueOf(getClass().getResource("/graphics/"+filename+".png")))
                );
                gp_viewer.setPreserveRatio(true);
                gp_viewer.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight() * 8);
                gp_container.getChildren().add(gp_viewer);
            }
        }
        return gp_container;
    }
}
