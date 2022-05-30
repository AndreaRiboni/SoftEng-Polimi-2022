package it.polimi.ingsw.view.main_game;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.view.Positions;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Deliverer {
    private final int ISLAND_SIZE = 200;
    private final int STUDENT_SIZE = ISLAND_SIZE/15;

    public ImageView getTowerImage(Color tower_color){
        ImageView tower = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Towers/"+Color.colorToString(tower_color)+"tower.png")))
        );
        tower.setFitWidth(Positions.TOWERS.getWidth());
        tower.setFitHeight(Positions.TOWERS.getHeight());
        return tower;
    }

    public ImageView getMotherNatureImage(){
        ImageView mn = new ImageView(
                new Image(String.valueOf(getClass().getResource("/MotherNature/mother_nature.gif")))
        );
        mn.setFitWidth(ISLAND_SIZE);
        mn.setFitHeight(ISLAND_SIZE);
        mn.getProperties().put("mothernature", true);
        mn.setEffect(new Glow(0.3));
        return mn;
    }

    public ImageView getProfessorImage(Color prof_color){
        ImageView prof = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Professors/" + Color.colorToString(prof_color) + "prof.png")))
        );
        prof.setFitWidth(Positions.PROFESSORS.getWidth());
        prof.setFitHeight(Positions.PROFESSORS.getHeight());
        return prof;
    }

    public ImageView getStudentImage(Color color){
        ImageView student = new ImageView(
                new Image(String.valueOf(getClass().getResource("/Students/"+Color.colorToString(color)+"stud.png")))
        );
        student.setFitWidth(Positions.DINING_HALL_STUDENTS.getWidth());
        student.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return student;
    }

    public Image getCharacterCardImage(int id){
        System.out.println("\tgetting cc image of id " + id + " (character_" + (id+1) + ".jpg)");
        //why the hell does character_7 returns character_8
        return new Image(String.valueOf(getClass().getResource("/characters/character_" + (id+1) + ".jpg")));
    }

    public ImageView getLockImage(){
        ImageView lock = new ImageView(
                new Image(String.valueOf(getClass().getResource("/characters/lock.png")))
        );
        lock.setPreserveRatio(true);
        lock.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return lock;
    }

    public ImageView getCoinImage(){
        ImageView coin = new ImageView(
                new Image(String.valueOf(getClass().getResource("/characters/coin.png")))
        );
        coin.setPreserveRatio(true);
        coin.setFitHeight(Positions.DINING_HALL_STUDENTS.getHeight());
        return coin;
    }

}
