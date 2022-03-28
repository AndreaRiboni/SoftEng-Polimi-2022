package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.EriantysException;

public class DiningHall extends StudentPlace {
    public static final int STUDENTS_PER_COLOR = 10;

    public DiningHall(){
        super(0);
    }

    @Override
    public void addStudent(Student student) throws EriantysException{
        long students_per_color;
        students_per_color = students.stream().filter(stud -> stud.getColor().equals(student.getColor())).count();

       if(students_per_color < STUDENTS_PER_COLOR) {
           students.add(student);
           if (students_per_color %3 == 0 && students_per_color > 0 ) {
               //TODO: qui ritornava vero per la moneta
           }
       }
       else{
           throw new EriantysException(String.format(EriantysException.INVALID_NOF_PLAYER, students_per_color));
       }
    }
}
