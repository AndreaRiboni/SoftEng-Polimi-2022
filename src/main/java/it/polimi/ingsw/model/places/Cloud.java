package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class Cloud implements StudentPlace {
    public static final boolean SIDE_2_4 = true, SIDE_3 = false;
    private final boolean side;
    private final List<Student> students;

    private Cloud(boolean side){
        this.side = side;
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
