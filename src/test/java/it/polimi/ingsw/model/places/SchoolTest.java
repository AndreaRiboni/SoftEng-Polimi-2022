package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {

    School school;

    @BeforeEach
    public void init(){
        try{
            school = new School(Color.WHITE, false);
        } catch (EriantysException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addStudentTest() throws EriantysException{
        Student[] yellow_students = new Student[9];
        for(int i=0; i<9; i++){
            yellow_students[i] = new Student(Color.YELLOW);
        }

        Student[] red_students = new Student[9];
        for(int i=0; i<9; i++){
            red_students[i] = new Student(Color.RED);
        }

        //two yellow students
        assertEquals(0, school.addStudent(yellow_students[0], Places.DINING_HALL));
        assertEquals(0, school.addStudent(yellow_students[1], Places.DINING_HALL));

        //the third yellow student
        assertEquals(1, school.addStudent(yellow_students[2], Places.DINING_HALL));

        //the first red student
        assertEquals(0, school.addStudent(red_students[0], Places.DINING_HALL));

        //3 more yellow students (TOT: 6 yellow students)
        assertEquals(0, school.addStudent(yellow_students[3], Places.DINING_HALL));
        assertEquals(0, school.addStudent(yellow_students[4], Places.DINING_HALL));
        assertEquals(1, school.addStudent(yellow_students[5], Places.DINING_HALL));

        //2 more red students (TOT: 3 red students)
        assertEquals(0, school.addStudent(red_students[1], Places.DINING_HALL));
        assertEquals(1, school.addStudent(red_students[2], Places.DINING_HALL));

        //3 more yellow students (TOT: 9 yellow students)
        assertEquals(0, school.addStudent(yellow_students[6], Places.DINING_HALL));
        assertEquals(0, school.addStudent(yellow_students[7], Places.DINING_HALL));
        assertEquals(1, school.addStudent(yellow_students[8], Places.DINING_HALL));

        //TODO: adding a 10th yellow student I expect an error
    }

}