package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

public class TowerHall implements StudentPlace, TowerPlace {
    @Override
    public void addStudent(Student student) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getStudent(Color color) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Student getRandomStudent() {
        throw new UnsupportedOperationException();
    }

    public boolean moveTowerTo(int island_index){ //forse inutile
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addTower(Tower tower) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getTower(Color color) {
        throw new UnsupportedOperationException();
    }
}
