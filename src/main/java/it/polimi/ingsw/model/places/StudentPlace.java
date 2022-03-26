package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class StudentPlace {
    protected final List<Student> students;
    protected final int MAX_STUDENTS;

    public StudentPlace(int max_stud){
        students = new ArrayList<>();
        MAX_STUDENTS = max_stud;
    }

    public boolean addStudent(Student student) {
        if(students.size() < MAX_STUDENTS) {
            students.add(student);
            return true;
        }
        return false;
    }

    public boolean popStudent(Color color) {
        int found = -1;
        for(int i = 0; i < students.size() && found==-1; i++){
            if(students.get(i).getColor().equals(color)) {
                found = i;
                students.remove(i);
            }
        }
        return found!=-1;
    }

    public Student getRandomStudent() {
        if(students.isEmpty()) return null;
        int index = (int)(Math.random() * students.size());
        Student student = students.get(index);
        students.remove(index);
        return student;
    }

    public List<Student> getStudents(){
        return students;
    }

    public void empty(){
        students.clear();
    }
}
