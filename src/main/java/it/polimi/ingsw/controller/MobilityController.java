package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.utils.GenericUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MobilityController extends Controller {
    private static final Logger log = LogManager.getRootLogger();

    public MobilityController(GameBoard model){
        super(model);
    }

    private boolean checkPlaces(Places[] places){
        for(Places sp : places){
            if(!(sp.equals(Places.ISLAND)) && !(sp.equals(Places.DINING_HALL))) return false;
        }
        return true;
    }

    public void move3Studs() throws EriantysException {
        //get the three students' colors and places
        Player player = model.getPlayers()[action.getPlayerID()];
        System.out.println("player " + player.getID());
        Map<Color, Integer> students = player.getEntranceStudents();
        System.out.println("entrance students: " + students);
        Color[] colors = action.getThreeStudents();
        System.out.println("colors: " + colors[0] + ", " + colors[1] + ", " + colors[2]);
        Places[] places = action.getThreeStudentPlaces();
        System.out.println("places: " + places[0] + ", " + places[1] + ", " + places[2]);
        int[] island_indexes = action.getIslandIndexes();
        System.out.println("islands: " + island_indexes[0] + ", " + island_indexes[1] + ", " + island_indexes[2]);
        if(!checkPlaces(places)){
            System.out.println("wrong places");
            throw new EriantysException(EriantysException.INVALID_PLACE);
        }
        //check that the selected students exist in the entrance
        Map<Color, Integer> new_students = GenericUtils.subtract(
                students,
                GenericUtils.listToMap(Arrays.asList(colors))
        );
        System.out.println("new students: " + new_students);
        if(!GenericUtils.isAllPositive(new_students)){
            log.error("Not enough students");
            throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
        }
        //these 3 studs have to be moved according to the corresponding studplace
        StringBuilder desc = new StringBuilder("Moving 3 students ");
        for(int i = 0; i < 3; i++){
            Color stud = colors[i];
            desc.append("(").append(stud).append(" in ");
            if(places[i].equals(Places.ISLAND)){
                desc.append("island ").append(island_indexes[i]);
                model.getIsland(island_indexes[i]).addStudent(stud);
            } else {
                desc.append("dining hall");
                player.moveStudentInDiningHall(stud);
            }
            desc.append(") ");
        }
        for(int i = 0; i < 3; i++){
            player.removeEntranceStudent(colors[i]);
        }
        log.info(desc.toString());
    }
}
