package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.Professor;
import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.ArrayList;
import java.util.List;

public class School {
    private final Entrance entrance;
    private final DiningHall dining_hall;
    private final TowerHall tower_hall;
    private final Color tower_color;
    private final List<Professor> professors;


    public School(Color color, boolean three_players) throws EriantysException {
        tower_color = color;
        entrance = new Entrance();
        dining_hall = new DiningHall();
        tower_hall = new TowerHall(tower_color, three_players);
        professors = new ArrayList<>();
        for(int i=0; i<7; i++) addStudent(Bag.getRandomStudent(), Places.ENTRANCE);
    }

    public void addStudent(Student student, Places place) throws EriantysException{
        switch(place) {
            case DINING_HALL: //TODO: coins
                dining_hall.addStudent(student);
                break;
            case ENTRANCE:
                entrance.addStudent(student);
                break;
            default:
                throw new EriantysException(EriantysException.INVALID_PLACE);
        }
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

    public void addProfessor(Professor prof) throws EriantysException{
        if(professors.contains(prof)) throw new EriantysException(EriantysException.DUPLICATE_PROFESSOR);
        professors.add(prof); //TODO: redundancy
    }

    public boolean popProfessor(Color col){
        for(int i = 0; i < professors.size(); i++){
            if(professors.get(i).getColor().equals(col)){
                professors.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getNumberOfProfs(){
        return professors.size();
    }

    public int getNumberOfTowers(){
        return tower_hall.getNumberOfTowers();
    }

    public List<Student> getEntranceStudents(){
        return entrance.getStudents();
    }
}
