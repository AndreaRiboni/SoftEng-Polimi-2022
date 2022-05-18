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
        Map<Color, Integer> students = player.getEntranceStudents();
        Color[] colors = action.getThreeStudents();
        Places[] places = action.getThreeStudentPlaces();
        int[] island_indexes = action.getIslandIndexes();
        if(!checkPlaces(places)){
            throw new EriantysException(EriantysException.INVALID_PLACE);
        }
        //check that the selected students exist in the entrance
        Map<Color, Integer> new_students = GenericUtils.subtract(
                students,
                GenericUtils.listToMap(Arrays.asList(colors))
        );
        if(!GenericUtils.isAllPositive(new_students)){
            log.error("Not enough students");
            throw new EriantysException(EriantysException.NOT_ENOUGH_STUDENTS);
        }
        //these 3 studs have to be moved according to the corresponding studplace
        StringBuilder desc = new StringBuilder("Moving the students ");
        int NOF_Players = model.getNofPlayers();
        for(int i = 0; i < NOF_Players + 1; i++){
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
        for(int i = 0; i < NOF_Players + 1; i++){
            player.removeEntranceStudent(colors[i]);
        }
        log.info(desc.toString());
    }
}
