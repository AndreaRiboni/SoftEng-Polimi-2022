package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.Map;

public class LockBehavior extends CardBehavior{

    public LockBehavior(GameBoard gameboard, int id, int nof_locks, Behaviors behavior_name) {
        super(gameboard, id, 0, 0, 0, 0, 0, nof_locks, false, false, false, behavior_name, 0);
    }

    @Override
    public Color[] getAvailableStudents() {
        EriantysException.throwUnsupportedOperation();
        return null;
    }

    @Override
    public int getNofTakeableStudents() {
        return 0;
    }

    @Override
    public int getAvailableLocks() {
        return 0;
    }

    @Override
    public boolean getStudent(Color color) {
        EriantysException.throwUnsupportedOperation();
        return false;
    }

    @Override
    public LockCard getLock() {
        //returns a usable lock
        for(int i = 0; i < lock_cards.length; i++){
            if(!lock_cards[i].isOnIsland()) return lock_cards[i];
        }
        return null;
    }

    @Override
    public boolean exchangeStudent(Color student1, Color student2) {
        EriantysException.throwUnsupportedOperation();
        return false;
    }

    @Override
    public void resetStudent(int index) {
        throw new UnsupportedOperationException();
    }
}
