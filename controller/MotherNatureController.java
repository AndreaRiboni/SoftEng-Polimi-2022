package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;

public class MotherNatureController extends Controller {
    public MotherNatureController(GameBoard model){
        super(model);
    }

    public void moveMotherNature() throws EriantysException {
        model.moveMotherNature(action.getMothernatureIncrement());
    }
}
