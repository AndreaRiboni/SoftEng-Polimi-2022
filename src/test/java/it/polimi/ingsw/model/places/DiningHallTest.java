package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiningHallTest {
    DiningHall diningHall;

    @BeforeEach
    public void init(){
        diningHall = new DiningHall();
    }

    /*@Test
    public void addStudentTest() throws EriantysException {
        Student[] yellow_students = new Student[10];
        for(int i=0; i< yellow_students.length; i++){
            yellow_students[i] = new Student(Color.YELLOW);
        }

        Student[] red_students = new Student[10];
        for(int i=0; i < red_students.length; i++){
            red_students[i] = new Student(Color.RED);
        }

        //two yellow students
        diningHall.addStudent(yellow_students[0]);
        diningHall.addStudent(yellow_students[1]);

        //the third yellow student
        diningHall.addStudent(yellow_students[2]);

        //the first red student
        diningHall.addStudent(red_students[0]);

        //3 more yellow students (TOT: 6 yellow students)
        diningHall.addStudent(yellow_students[3]);
        diningHall.addStudent(yellow_students[4]);
        diningHall.addStudent(yellow_students[5]);

        //2 more red students (TOT: 3 red students)
        diningHall.addStudent(red_students[1]);
        diningHall.addStudent(red_students[2]);

        //4 more yellow students (TOT: 10 yellow students)
        diningHall.addStudent(yellow_students[6]);
        diningHall.addStudent(yellow_students[7]);
        diningHall.addStudent(yellow_students[8]);
        diningHall.addStudent(yellow_students[9]);

        //adding a 11th yellow student I expect an error
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            diningHall.addStudent(new Student(Color.YELLOW));
        });

      }
     */

}