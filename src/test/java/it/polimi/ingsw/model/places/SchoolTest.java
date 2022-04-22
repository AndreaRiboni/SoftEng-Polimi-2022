package it.polimi.ingsw.model.places;

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
            GameBoard gameboard = new GameBoard();
            school = new School(Color.WHITE, false, gameboard);
            System.out.println(school);
        } catch (EriantysException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addStudentTest() throws EriantysException{
        //two yellow students
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));

        //the third yellow student
        assertEquals(1, school.addStudent(Color.YELLOW, Places.DINING_HALL));

        //the first red student
        assertEquals(0, school.addStudent(Color.RED, Places.DINING_HALL));

        //3 more yellow students (TOT: 6 yellow students)
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));
        assertEquals(1, school.addStudent(Color.YELLOW, Places.DINING_HALL));

        //2 more red students (TOT: 3 red students)
        assertEquals(0, school.addStudent(Color.RED, Places.DINING_HALL));
        assertEquals(1, school.addStudent(Color.RED, Places.DINING_HALL));

        //4 more yellow students (TOT: 10 yellow students)
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));
        assertEquals(1, school.addStudent(Color.YELLOW, Places.DINING_HALL));
        assertEquals(0, school.addStudent(Color.YELLOW, Places.DINING_HALL));

        //adding a 11th yellow student I expect an error
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            school.addStudent(Color.YELLOW, Places.DINING_HALL);
        });

        //adding a student in a place different than the dining hall and the entrance throws an exception
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            school.addStudent(Color.RED, Places.CLOUD);
        });

        //adding more than 10 pink students to the entrance throws an exception
        for(int i = 0; i < 10; i++){
            school.addStudent(Color.PINK, Places.DINING_HALL);
        }
        thrown = Assertions.assertThrows(EriantysException.class, () -> {
            school.addStudent(Color.PINK, Places.DINING_HALL);
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
        for(int i=0; i<3; i++) {
            school.addStudent(Color.GREEN, Places.DINING_HALL);
        }
        for(int i=3; i<6; i++) {
            school.addStudent(Color.BLUE, Places.DINING_HALL);
        }
        for(int i=6; i<9; i++) {
            school.addStudent(Color.RED, Places.DINING_HALL);
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
        while(school.getNumberOfTowers() > 0){
            assertTrue(school.removeTower());
        }
        assertFalse(school.removeTower());
    }

}