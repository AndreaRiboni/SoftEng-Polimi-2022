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
    public boolean getLock() {
        //returns true if there is an available lock
        if(nof_locks > 0){
            nof_locks--;
            return true;
        }
        return false;
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
