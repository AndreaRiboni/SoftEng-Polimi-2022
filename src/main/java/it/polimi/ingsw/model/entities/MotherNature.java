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

    public Color calculateInfluence(){ //Creare un metodo statico su Island per calcolare un'influenza generica
            Island current = gameboard.getIsland(island_index);
            if(current.isLocked()){
                current.unlock();
                current.getLock().removeFromIsland();
                if(current.getTower(Color.WHITE)) return Color.WHITE;
                if(current.getTower(Color.GREY)) return Color.GREY;
                if(current.getTower(Color.BLACK)) return Color.BLACK;
                return null;
                //return gameboard.getIsland(island_index).getInfluent(); //da definire
            } else {
                return current.calculateInfluence();
            }
    }
}
