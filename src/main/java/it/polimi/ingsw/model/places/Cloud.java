package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class Cloud extends StudentPlace {
    public static final boolean SIDE_2_4 = true, SIDE_3 = false;
    public static final int MAX_STUDENTS_2_4 = 4, MAX_STUDENTS_3 = 3;
    private final int MAX_STUDENTS;
    private final boolean side;
    private final List<Student> students;

    private Cloud(boolean side){
        super(0);
        this.side = side;
        MAX_STUDENTS = side == SIDE_2_4 ? MAX_STUDENTS_2_4 : MAX_STUDENTS_3;
        students = new ArrayList<>();
    }

    public Cloud create2or4PlayerCloud(){
        return new Cloud(SIDE_2_4);
    }

    public Cloud create3PlayerCloud(){
        return new Cloud(SIDE_3);
    }

    public boolean getSide(){
        return side;
    }

    @Override
    public boolean addStudent(Student student) {
        if(students.size() < MAX_STUDENTS){
            students.add(student);
            return true;
        }
        return false;
    }

    @Override
    public boolean popStudent(Color color) {
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
