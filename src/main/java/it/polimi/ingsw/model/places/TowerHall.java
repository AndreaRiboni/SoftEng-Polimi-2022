package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;

import java.util.List;

public class TowerHall implements StudentPlace, TowerPlace {
    private static List<Tower> towers;
    private final List<Student> students;

    public TowerHall(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean getTower(Color color) {
        int to_remove = -1;
        for(int i = 0; i < towers.size() && to_remove==-1; i++){
            if(towers.get(i).getColor().equals(color)){
                to_remove = i;
            }
        }
        towers.remove(to_remove);
        return true;
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
    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public boolean addTower(Tower tower) {
        if(towers.size() < 8){
            return towers.add(tower);
        }
        else{
            throw new UnsupportedOperationException();
        }
    }

    public static void remove(Tower tower){
        if(towers.size() > 0){
            towers.remove(tower);
        }
        else{throw new UnsupportedOperationException();}
    }

    @Override
    public Student getRandomStudent() {
        int index = (int)(Math.random() * students.size());
        Student student = students.get(index);
        students.remove(student);
        return student;
    }

}
