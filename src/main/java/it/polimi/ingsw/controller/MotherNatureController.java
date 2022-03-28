package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;
import it.polimi.ingsw.model.utils.EriantysException;

public class MotherNatureController extends Controller {
    public MotherNatureController(GameBoard model){
        super(model);
    }

    public void moveMotherNature() throws EriantysException {
        //TODO: posso muovermi di questo numero di passi? Verificare con la carta assistente
        //action.getPlayerID() --> getLastPlayedCard() --> getValue >= mn_increment
        model.moveMotherNature(action.getMothernatureIncrement());
    }
}
