package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;

public abstract class Controller {
    protected Action action;
    protected final GameBoard model;

    public Controller(GameBoard model){
        this.model = model;
    }

    /**
     * sets the action to be analyzed
     * @param action action to refer to
     */
    public void setAction(Action action){
        this.action = action;
    }

}
