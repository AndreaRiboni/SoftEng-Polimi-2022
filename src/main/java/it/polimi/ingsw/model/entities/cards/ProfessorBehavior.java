package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

import java.util.Map;

public class ProfessorBehavior extends CardBehavior {
    public ProfessorBehavior(GameBoard gameboard, int id, Behaviors behavior_name) {
        super(gameboard, id,0, 0, 0, 0, 0, 0, false, false, false, behavior_name, 0);
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
        EriantysException.throwUnsupportedOperation();
        return 0;
    }

    @Override
    public boolean getStudent(Color color) {
        EriantysException.throwUnsupportedOperation();
        return false;
    }

    @Override
    public boolean getLock() {
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
