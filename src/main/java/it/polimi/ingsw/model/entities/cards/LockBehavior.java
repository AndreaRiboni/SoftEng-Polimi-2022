package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class LockBehavior extends CardBehavior{

    public LockBehavior(GameBoard gameboard, int id, int nof_locks) {
        super(gameboard, id, 0, 0, 0, 0, 0, nof_locks, false, false, false);
    }

    @Override
    public Student[] getAvailableStudents() {
        EriantysException.throwUnsupportedOperation();
        return null;
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
    public boolean exchangeStudent(Student student1, Student student2) {
        EriantysException.throwUnsupportedOperation();
    }

    @Override
    public void preTurnEffect() {
        //todo
    }

    @Override
    public void postTurnEffect() {
        //todo
    }
}
