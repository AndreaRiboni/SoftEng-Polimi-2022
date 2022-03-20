package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

public class School {
    private Entrance entrance;
    private DiningHall dining_hall;
    private TowerHall tower_hall;


    public void addStudent(Student student, StudentPlace place){
        if(place.equals("Dining")) {DiningHall.addStudent(student);}
        else if(place.equals("Entrance")){Entrance.addStudent(student);}
        else {
            throw new UnsupportedOperationException();
        }
    }

    public static void TowerRemove(Tower tower){
        TowerHall.remove(tower);
    }

    public int getNumberOfStudents(Color col){
        int count = 0;
        int flag = 0;
        for(;;){
            if(Student.color.equals(col)) count++;
        }
        if(count == 3 && flag == 0){
            Player.addCoins(1);
            flag = 1;
        }
        if(count == 6 && flag==1){
            Player.addCoins(1);
            flag = 2;
        }
        if(count == 9 && flag == 2){
            Player.addCoins(1);
            flag = 3;
        }
        return count;
    }
}
