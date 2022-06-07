package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.io.Serializable;

/**
 * Dining hall area
 */
public class DiningHall extends StudentPlace implements Serializable {
    /**
     * How many students for each color maximum
     */
    public static final int STUDENTS_PER_COLOR = 10;

    /**
     * Creates the dining hall
     */
    public DiningHall(){
        super(0);
    }

    @Override
    public void addStudent(Color student) throws EriantysException{
        int students_per_color = students.getOrDefault(student, 0);

       if(students_per_color < STUDENTS_PER_COLOR) {
           StudentPlace.incrementMapValue(students, student, 1);
       }
       else{
           throw new EriantysException(String.format(EriantysException.INVALID_NOF_PLAYER, students_per_color));
       }
    }
}
