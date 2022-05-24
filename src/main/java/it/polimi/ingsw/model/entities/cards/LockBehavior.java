package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

import java.io.Serializable;

public class LockBehavior extends CardBehavior implements Serializable {

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
