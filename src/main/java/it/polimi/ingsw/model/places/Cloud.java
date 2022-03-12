package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.entities.Student;

public class Cloud implements StudentPlace {
    public static final boolean SIDE_2_4 = true, SIDE_3 = false;
    private boolean side;

    public Cloud(boolean side){
        this.side = side;
    }

    public boolean getSide(){
        return side;
    }

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
