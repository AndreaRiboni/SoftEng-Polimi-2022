package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class Bag implements StudentPlace {
    private final List<Student> students;

    public Bag() {
        students = new ArrayList<>();
    }

    @Override
    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public boolean getStudent(Color color) {
        int to_remove = -1;
        for(int i = 0; i < students.size() && to_remove==-1; i++){
            if(students.get(i).getColor().equals(color)){
                to_remove = i;
            }
        }
        students.remove(to_remove);
        return true;
    }

    @Override
    public Student getRandomStudent() {
        int index = (int)(Math.random() * students.size());
        Student student = students.get(index);
        students.remove(student);
        return student;
    }
}
