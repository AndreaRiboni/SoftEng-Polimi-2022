package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.places.Island;

public class MotherNature {
    private int island_index;
    private final GameBoard gameboard;

    public MotherNature(GameBoard gameboard, int island_index){
        this.island_index = island_index;
        this.gameboard = gameboard;
    }

    public void stepForward(int steps) throws EriantysException {
        EriantysException.throwInvalidSteps(steps);
        island_index += steps;
        island_index %= GameBoard.NOF_ISLAND;
    }

    public Color calculateInfluence(){ //TBD
            Island current = gameboard.getIsland(island_index);
            if(current.isLocked()){
                current.unlock();
                return Color.getRandomColor(); //da definire
            } else {
                return current.calculateInfluence();
            }
    }
}