package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import it.polimi.ingsw.model.places.Island;

public class MotherNature {
    private int island_index;
    private final GameBoard gameboard;
    private boolean avoid_towers;
    private Color avoid_color;

    public MotherNature(GameBoard gameboard, int island_index){
        this.island_index = island_index;
        this.gameboard = gameboard;
        avoid_towers = false;
        avoid_color = null;
    }

    public void stepForward(int steps) throws EriantysException {
        if(steps <= 0 || steps > AssistCard.MAX_STEPS)
            throw new EriantysException(
                    String.format(EriantysException.INVALID_STEPS, steps)
            );
        island_index += steps;
        island_index %= GameBoard.NOF_ISLAND;
    }

    public Color calculateInfluence() throws EriantysException {
            Island current = gameboard.getIsland(island_index);
            return current.calculateInfluence(avoid_towers, avoid_color);
    }

    public void endTurn(){
        avoid_towers = false;
        avoid_color = null;
    }

    public void avoidTowers(){
        avoid_towers = true;
    }

    public void avoidColor(Color col){
        avoid_color = col;
    }

    public boolean hasToAvoidTowers(){
        return avoid_towers;
    }

    public Color hasToAvoidColor(){
        return avoid_color;
    }

    public String toString(){
        return "mothernature on " + island_index;
    }
}
