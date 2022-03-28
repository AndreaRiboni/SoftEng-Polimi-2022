package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class DiningHall extends StudentPlace {
    public static final int STUDENTS_PER_COLOR = 10;

    public DiningHall(){
        super(0);
    }

    @Override
    public void addStudent(Student student) throws EriantysException{
        int students_per_color = getNofStudents(student.getColor());

       if(students_per_color < STUDENTS_PER_COLOR) {
           students.add(student);
       }
       else{
           throw new EriantysException(String.format(EriantysException.INVALID_NOF_PLAYER, students_per_color));
       }
    }

    public int getNofStudents(Color col){
        return (int)students.stream().filter(stud -> stud.getColor().equals(col)).count();
    }
}
