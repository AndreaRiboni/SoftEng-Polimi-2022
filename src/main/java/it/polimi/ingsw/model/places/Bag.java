package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.entities.Student;

public class Bag implements StudentPlace {

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
}
