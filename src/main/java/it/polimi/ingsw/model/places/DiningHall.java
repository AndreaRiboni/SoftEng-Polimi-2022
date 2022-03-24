package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;

public class DiningHall extends StudentPlace {
    public static final int STUDENTS_PER_COLOR = 10;

    public DiningHall(){
        super(0);
    }
    
    public boolean addStudent(Student student, Player player){
        if(students.stream().filter(stud -> stud.getColor().equals(student.getColor())).count() < STUDENTS_PER_COLOR) {
            students.add(student);
            if (students.stream().filter(stud -> stud.getColor().equals(student.getColor())).count() %3 == 0 && 
                    students.stream().filter(stud -> stud.getColor().equals(student.getColor())).count() > 0 ) {
               player.addCoins(1);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addStudent(Student student) {
        if(students.stream().filter(stud -> stud.getColor().equals(student.getColor())).count() < STUDENTS_PER_COLOR) {
            students.add(student);
            return true;
        }
        return false;
    }
}
