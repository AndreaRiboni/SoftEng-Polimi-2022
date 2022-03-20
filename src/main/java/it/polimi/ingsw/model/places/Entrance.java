package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import jdk.internal.icu.text.UnicodeSet;

import java.util.List;

public class Entrance implements StudentPlace {
    private static List<Student> EntranceStudents;

    @Override
    public static void addStudent(Student student) {
        if(EntranceStudents.size() < 8 && EntranceStudents.size() > 0){
            EntranceStudents.add(student);
        }
        else{throw new UnsupportedOperationException();}
    }

    @Override
    public boolean getStudent(Color color) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Student getRandomStudent() {
        throw new UnsupportedOperationException();
    }

    public static void remove(Student student){
        if(EntranceStudents.size() > 0){
            EntranceStudents.remove(student);
        }
        else{throw new UnsupportedOperationException();}
    }
}
