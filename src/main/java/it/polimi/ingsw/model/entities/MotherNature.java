package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.places.GameBoard;

public class MotherNature {
    private int island_index;
    private final GameBoard gameboard;

    public MotherNature(GameBoard gameboard, int island_index){
        this.island_index = island_index;
        this.gameboard = gameboard;
    }

    public void stepForward(int steps){
        island_index += steps;
    }

    public void calculateInfluence(){
        throw new UnsupportedOperationException();
    }
}
