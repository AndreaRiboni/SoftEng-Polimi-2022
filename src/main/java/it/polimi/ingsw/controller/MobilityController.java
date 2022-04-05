package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Places;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.List;
import java.util.Map;

public class MobilityController extends Controller {
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
        //get the three students' indexes and places
        Player player = model.getPlayers()[action.getPlayerID()];
        Map<Color, Integer> students = player.getEntranceStudents();
        //TODO: indexes have to be different
        Color[] colors = action.getThreeStudents();
        Places[] places = action.getThreeStudentPlaces();
        if(!checkPlaces(places)){
            throw new EriantysException(EriantysException.INVALID_PLACE);
        }
        //these 3 studs have to be moved according to the corresponding studplace
        for(int i = 0; i < 3; i++){
            Color stud = colors[i];
            if(places[i].equals(Places.ISLAND)){
                model.getIsland(places[i].getExtraValue()).addStudent(stud);
            } else {
                player.moveStudentInDiningHall(stud);
            }
        }
        for(int i = 0; i < 3; i++){
            player.removeEntranceStudent(colors[i]);
        }
    }
}
