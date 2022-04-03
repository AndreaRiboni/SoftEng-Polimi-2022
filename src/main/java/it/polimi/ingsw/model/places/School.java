package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.ArrayList;
import java.util.List;

public class School {
    private final Entrance entrance;
    private final DiningHall dining_hall;
    private final TowerHall tower_hall;
    private final Color tower_color;


    public School(Color color, boolean three_players) throws EriantysException {
        tower_color = color;
        entrance = new Entrance();
        dining_hall = new DiningHall();
        tower_hall = new TowerHall(tower_color, three_players);
        for(int i=0; i<7; i++) addStudent(Bag.getRandomStudent(), Places.ENTRANCE);
    }

    public int addStudent(Student student, Places place) throws EriantysException{
        switch(place) {
            case DINING_HALL:
                dining_hall.addStudent(student);
                int nof_studs = dining_hall.getNofStudents(student.getColor());
                if(nof_studs % 3 == 0 && nof_studs > 0){
                    return 1;
                }
                break;
            case ENTRANCE:
                entrance.addStudent(student);
                break;
            default:
                throw new EriantysException(EriantysException.INVALID_PLACE);
        }
        return 0;
    }

    public int getNumberOfStudents(Color col, Places place){
        StudentPlace sp = null;
        switch(place) {
        case DINING_HALL:
            sp = dining_hall;
            break;
        case ENTRANCE:
            sp = entrance;
            break;
        default:
            return -1;
        }
        return (int)sp.getStudents().stream().filter(stud->stud.getColor().equals(col)).count();
    }

    public boolean removeStudent(Color color, Places place){
        StudentPlace sp = null;
        switch(place) {
            case DINING_HALL:
                sp = dining_hall;
                break;
            case ENTRANCE:
                sp = entrance;
                break;
            default:
                return false;
        }
        for(int i = 0; i < sp.getStudents().size(); i++){
            if(sp.getStudents().get(i).getColor().equals(color)){
                sp.getStudents().remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeTower(){
        return tower_hall.getTower(tower_color);
    }

    public int getNumberOfTowers(){
        return tower_hall.getNumberOfTowers();
    }

    public List<Student> getEntranceStudents(){
        return entrance.getStudents();
    }

    public List<Student> getDiningStudents(){
        return dining_hall.getStudents();
    }

    public void swapEntranceDining(int entrance_index, int dining_index) throws EriantysException {
        if(entrance_index < 0 || entrance_index > getEntranceStudents().size() || dining_index < 0 || dining_index > getDiningStudents().size()){
            throw new EriantysException(EriantysException.INVALID_STUDENT_INDEX);
        }
        List<Student> entrance = getEntranceStudents();
        List<Student> dining = getDiningStudents();
        Student entrance_s = entrance.get(entrance_index);
        Student dining_s = dining.get(dining_index);
        entrance.remove(entrance_s);
        dining.remove(dining_s);
        entrance.add(dining_s);
        dining.add(entrance_s);
    }

    public boolean removeFromDiningHall(Color c){
        for(int i = 0; i < getDiningStudents().size(); i++){
            if(getDiningStudents().get(i).getColor().equals(c)){
                getDiningStudents().remove(i);
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("entrance: ").append(entrance).append("\n");
        sb.append("dininghall: ").append(dining_hall).append("\n");
        sb.append("towerhall: ").append(tower_hall).append("\n");
        return sb.toString();
    }

    public void addTower(Tower tower) throws EriantysException {
        tower_hall.addTower(tower);
    }
}
