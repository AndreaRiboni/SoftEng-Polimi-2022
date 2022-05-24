package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Action;

public abstract class Controller {
    protected Action action;
    protected final GameBoard model;

    public Controller(GameBoard model){
        this.model = model;
    }

    public void setAction(Action action){
        this.action = action;
    }
}
