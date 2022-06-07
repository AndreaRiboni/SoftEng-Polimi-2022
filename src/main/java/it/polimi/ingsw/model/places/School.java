package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Player's school
 */
public class School implements Serializable {
    private final Entrance entrance;
    private final DiningHall dining_hall;
    private final TowerHall tower_hall;
    private final Color tower_color;

    /**
     * Creates the school
     * @param color tower color
     * @param three_players true if 3 players mode
     * @param gameboard model of reference
     * @throws EriantysException game-semantic error
     */
    public School(Color color, boolean three_players, GameBoard gameboard) throws EriantysException {
        tower_color = color;
        entrance = new Entrance(three_players);
        dining_hall = new DiningHall();
        tower_hall = new TowerHall(tower_color, three_players);
        for(int i=0; i < (three_players ? 9 : 7); i++) addStudent(gameboard.drawFromBag(), Places.ENTRANCE);
    }

    /**
     * adds a student into a school area
     * @param student student to add
     * @param place places to add the student into (dining hall or entrance)
     * @return number of coins to add
     * @throws EriantysException game-semantic error
     */
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

    /**
     * gets the number of the students of a color in a school area
     * @param col color of the students to count
     * @param place places to add the student into (dining hall or entrance)
     * @return number of these students
     */
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

    /**
     * removes a student from a school area
     * @param color color to remove
     * @param place dining hall or entrance
     * @return true if the student has been correctly removed, false otherwise
     */
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

    /**
     * removes a tower from the tower hall
     * @return true if the tower has been correctly removed
     */
    public boolean removeTower(){
        return tower_hall.getTower(tower_color);
    }

    /**
     * @return number of towers in the towerhall
     */
    public int getNumberOfTowers(){
        return tower_hall.getNumberOfTowers();
    }

    /**
     * @return students in the entrance
     */
    public Map<Color, Integer> getEntranceStudents(){
        return entrance.getStudents();
    }

    /**
     * @return students in the dining hall
     */
    public Map<Color, Integer> getDiningStudents(){
        return dining_hall.getStudents();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t").append(GenericUtils.toBold("Entrance")).append(":\n").append(entrance).append("\n");
        sb.append("\t\t").append(GenericUtils.toBold("Dining Hall")).append(":\n").append(dining_hall).append("\n");
        sb.append("\t\t").append(GenericUtils.toBold("Tower Hall")).append(":\n").append(tower_hall).append("\n");
        return sb.toString();
    }

    /**
     * adds a tower in the tower hall
     * @param tower tower to add
     * @throws EriantysException game-semantic error
     */
    public void addTower(Color tower) throws EriantysException {
        tower_hall.addTower(tower);
    }

    /**
     * @return this school's tower color
     */
    public Color getTowerColor(){
        return tower_color;
    }
}
