package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Tower;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.List;
import java.util.Map;

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

    public int addStudent(Color student, Places place) throws EriantysException{
        switch(place) {
            case DINING_HALL:
                dining_hall.addStudent(student);
                int nof_studs = dining_hall.countByColor(student);
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
        return sp.getStudents().getOrDefault(col, 0);
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
        return StudentPlace.incrementMapValue(sp.getStudents(), color, -1);
    }

    public boolean removeTower(){
        return tower_hall.getTower(tower_color);
    }

    public int getNumberOfTowers(){
        return tower_hall.getNumberOfTowers();
    }

    public Map<Color, Integer> getEntranceStudents(){
        return entrance.getStudents();
    }

    public Map<Color, Integer> getDiningStudents(){
        return dining_hall.getStudents();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("entrance:\n").append(entrance).append("\n");
        sb.append("dininghall:\n").append(dining_hall).append("\n");
        sb.append("towerhall:\n").append(tower_hall).append("\n");
        return sb.toString();
    }

    public void addTower(Tower tower) throws EriantysException {
        tower_hall.addTower(tower);
    }
}
