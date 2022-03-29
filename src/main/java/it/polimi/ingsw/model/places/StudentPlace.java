package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.ArrayList;
import java.util.List;

public abstract class StudentPlace {
    protected final List<Student> students;
    protected final int MAX_STUDENTS;

    public StudentPlace(int max_stud){
        students = new ArrayList<>();
        MAX_STUDENTS = max_stud;
    }

    public void addStudent(Student student) throws EriantysException{
        if(students.size() < MAX_STUDENTS) {
            students.add(student);
        } else {
            throw new EriantysException(EriantysException.STUDENTPLACE_FULL);
        }
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

    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(students.isEmpty()) sb.append("empty");
        for(Student s : students){
            sb.append("[").append(s).append("]");
        }
        return sb.toString();
    }
}
