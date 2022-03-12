package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;

public class School {
    private Entrance entrance;
    private DiningHall dining_hall;
    private TowerHall tower_hall;

    public void addStudent(Student student, StudentPlace place){
        throw new UnsupportedOperationException();
    }

    public int getNumberOfStudents(Color col){
        throw new UnsupportedOperationException();
    }
}
