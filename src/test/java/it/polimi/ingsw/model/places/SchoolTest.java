package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
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
        Student[] yellow_students = new Student[10];
        for(int i=0; i< yellow_students.length; i++){
            yellow_students[i] = new Student(Color.YELLOW);
        }

        Student[] red_students = new Student[10];
        for(int i=0; i < red_students.length; i++){
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

        //4 more yellow students (TOT: 10 yellow students)
        assertEquals(0, school.addStudent(yellow_students[6], Places.DINING_HALL));
        assertEquals(0, school.addStudent(yellow_students[7], Places.DINING_HALL));
        assertEquals(1, school.addStudent(yellow_students[8], Places.DINING_HALL));
        assertEquals(0, school.addStudent(yellow_students[9], Places.DINING_HALL));

        //adding a 11th yellow student I expect an error
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            school.addStudent(new Student(Color.YELLOW), Places.DINING_HALL);
        });

        //adding a student in a place different than the dining hall and the entrance throws an exception
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            school.addStudent(new Student(Color.RED), Places.CLOUD);
        });

        //adding more than 7 the red students to the entrance throws an exception (the entrance is initialized with 7 students)
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            school.addStudent(new Student(Color.PINK), Places.ENTRANCE);
        });

    }

    @Test
    void removeStudentTest() throws EriantysException {
        //test: i can remove a student from DINING_HALL and ENTRANCE only
        //      when i choose the wrong StudentPlace i get return false
        assertFalse(school.removeStudent(Color.GREEN, Places.CLOUD));
        assertFalse(school.removeStudent(Color.RED, Places.ISLAND));
        //test: the number of student of that color in that studentplace is decreased by one
        //      the number of student of other colors doesn't change
        Student[] students = new Student[9];
        for(int i=0; i<3; i++) {
            students[i] = new Student(Color.GREEN);
            school.addStudent(students[i], Places.DINING_HALL);
        }
        for(int i=3; i<6; i++) {
            students[i] = new Student(Color.BLUE);
            school.addStudent(students[i], Places.DINING_HALL);
        }
        for(int i=6; i<9; i++) {
            students[i] = new Student(Color.RED);
            school.addStudent(students[i], Places.DINING_HALL);
        }
        school.removeStudent(Color.GREEN, Places.DINING_HALL);
        assertEquals(2, school.getNumberOfStudents(Color.GREEN, Places.DINING_HALL));
        assertEquals(3, school.getNumberOfStudents(Color.BLUE, Places.DINING_HALL));
        assertEquals(3, school.getNumberOfStudents(Color.RED, Places.DINING_HALL));

        //test: if i can't remove any students i get return false
        //test: if the student is removed i get return true
        assertFalse(school.removeStudent(Color.YELLOW, Places.DINING_HALL));
        assertTrue(school.removeStudent(Color.RED, Places.DINING_HALL));
        assertTrue(school.removeStudent(Color.RED, Places.DINING_HALL));
        assertTrue(school.removeStudent(Color.RED, Places.DINING_HALL));
        assertFalse(school.removeStudent(Color.RED, Places.DINING_HALL));
    }

    @Test
    void removeTowerTest() throws EriantysException {
        //test: it returns true if the tower is removed ( ==> number of towers decreased)
        Tower t1 = new Tower(Color.WHITE);
        school.addTower(t1);
        assertTrue(school.removeTower());
        //test: it return false if there aren't enough towers / wrong tower color
        assertFalse(school.removeTower());
        Tower t2 = new Tower(Color.GREY);
        assertFalse(school.removeTower());
    }
}