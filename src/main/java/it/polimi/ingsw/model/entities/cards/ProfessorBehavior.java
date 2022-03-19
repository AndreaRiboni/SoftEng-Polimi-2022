package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class ProfessorBehavior extends CardBehavior {
    public ProfessorBehavior(GameBoard gameboard, int id) {
        super(gameboard, id,0, 0, 0, 0, 0, 0, false, false, false);
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
    public void addStudent(Student student) {
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
