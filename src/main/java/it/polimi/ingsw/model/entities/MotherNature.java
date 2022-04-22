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
        int old_island_index = island_index;
        if(steps <= 0 || steps > AssistCard.MAX_STEPS)
            throw new EriantysException(
                    String.format(EriantysException.INVALID_STEPS, steps)
            );
        while(steps>0){
            Island current = gameboard.getIsland(island_index);
            while(current.getNext()!=null){
                current = current.getNext();
            }
            island_index = (current.getIndex() + 1) % GameBoard.NOF_ISLAND;
            steps--;

        }
        gameboard.getIsland(island_index).setMotherNature();
        gameboard.getIsland(old_island_index).unsetMotherNature();
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

    public int getIslandIndex(){
        return island_index;
    }

    /**
     * @param index
     * @warning only for test
     */
    public void setIslandIndex(int index){
        island_index = index;
    }

    public String toString(){
        return "mothernature on " + island_index;
    }
}
