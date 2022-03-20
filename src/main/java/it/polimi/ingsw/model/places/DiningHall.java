package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;

import java.util.List;

public class DiningHall implements StudentPlace {
    private static List<Student> DiningStudents;

    @Override
    public static void addStudent(Student student) {
        Color col = Student.getColor(student);
        if(DiningHall.getStudent(col) < 10){
            DiningStudents.add(student);
        }
        else{throw new UnsupportedOperationException();}
    }

   @Override
    public static int getStudent(Color color) {
        int count = 0;
        for(;;){
            if(Student.color == col) count++;
        }
        return count;
    }

    @Override
    public Student getRandomStudent() {
        int index = (int)(Math.random() * DiningStudents.size());
        Student student = DiningStudents.get(index);
        DiningStudents.remove(student);
        return student;
    }
}
