package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.entities.cards.AssistCard;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.io.Serializable;

/**
 * Mother nature
 */
public class MotherNature implements Serializable {
    private int island_index;
    private final GameBoard gameboard;
    private boolean avoid_towers;
    private Color avoid_color;

    /**
     * Creates mother nature
     * @param gameboard model of reference
     * @param island_index starting island index
     * @throws EriantysException game-semantic error
     */
    public MotherNature(GameBoard gameboard, int island_index) throws EriantysException {
        this.island_index = island_index;
        this.gameboard = gameboard;
        this.gameboard.getIsland(this.island_index).setMotherNature();
        avoid_towers = false;
        avoid_color = null;
    }

    /**
     * Lets mother nature walk this number of steps
     * @param steps number of steps
     * @throws EriantysException game-semantic error
     */
    public void stepForward(int steps) throws EriantysException {
        int old_island_index = island_index;
        if(steps <= 0 || steps > AssistCard.MAX_STEPS + 2)
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

    /**
     * calculates the islands on which she stands on's influence
     * @return influent color
     * @throws EriantysException game-semantic error
     */
    public Color calculateInfluence() throws EriantysException {
            Island current = gameboard.getIsland(island_index);
            return current.calculateInfluence(avoid_towers, avoid_color);
    }

    /**
     * resets mother nature's additional effects
     */
    public void endTurn(){
        avoid_towers = false;
        avoid_color = null;
    }

    /**
     * sets mother nature to not consider towers when calculating the influence
     */
    public void avoidTowers(){
        avoid_towers = true;
    }

    /**
     * sets mother nature to not consider a particular color when calculating the influence
     * @param col color to avoid
     */
    public void avoidColor(Color col){
        avoid_color = col;
    }

    /**
     * @return true if mother nature is avoiding towers
     */
    public boolean hasToAvoidTowers(){
        return avoid_towers;
    }

    /**
     * @return the color mother nature is avoiding or null if she isn't avoiding a color
     */
    public Color hasToAvoidColor(){
        return avoid_color;
    }

    /**
     * @return index of the island she stands on
     */
    public int getIslandIndex(){
        return island_index;
    }

    /**
     * @param index index of the islands to set on
     * @deprecated only for test
     */
    public void setIslandIndex(int index){
        island_index = index;
    }

    public String toString(){
        return "mothernature on " + island_index;
    }

}
