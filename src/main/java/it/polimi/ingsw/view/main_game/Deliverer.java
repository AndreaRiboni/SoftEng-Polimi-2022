package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.view.Positions;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
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
        System.out.println("\tgetting cc image of id " + id + " (character_" + (id+1) + ".jpg)");
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
        System.out.println("Lock image downloaded");
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

}
