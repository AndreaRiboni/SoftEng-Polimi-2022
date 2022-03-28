package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class ProfessorBehavior extends CardBehavior {
    public ProfessorBehavior(GameBoard gameboard, int id, Behaviors behavior_name) {
        super(gameboard, id,0, 0, 0, 0, 0, 0, false, false, false, behavior_name);
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
    public LockCard getLock() {
        return null;
    }

    @Override
    public boolean exchangeStudent(Student student1, Student student2) {
        EriantysException.throwUnsupportedOperation();
        return false;
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
