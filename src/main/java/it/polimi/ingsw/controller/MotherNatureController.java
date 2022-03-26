package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;

public class MotherNatureController extends Controller {
    public MotherNatureController(GameBoard model){
        super(model);
    }

    public void moveMotherNature(){
        model.moveMotherNature(action.getMothernatureIncrement());
    }
}
