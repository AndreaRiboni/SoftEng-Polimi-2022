package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

import java.io.Serializable;

/**
 * behavior of a no entry tile's container card
 */
public class LockBehavior extends CardBehavior implements Serializable {

    /**
     * Creates the behavior
     * @param gameboard model of reference
     * @param id card's id
     * @param nof_locks number of available locks
     * @param behavior_name name
     */
    public LockBehavior(GameBoard gameboard, int id, int nof_locks, Behaviors behavior_name) {
        super(gameboard, id, 0, 0, 0, 0, 0, nof_locks, false, false, false, behavior_name, 0);
    }

    @Override
    public int getAvailableLocks() {
        return nof_locks;
    }

    @Override
    public boolean getLock() {
        //returns true if there is an available lock
        if(nof_locks > 0){
            nof_locks--;
            return true;
        }
        return false;
    }
}
