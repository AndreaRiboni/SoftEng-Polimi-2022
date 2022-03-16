package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class ProfessorBehavior extends CardBehavior {
    public ProfessorBehavior(GameBoard gameboard) {
        super(gameboard);
    }

    @Override
    public Student[] getAvailableStudents() {
        EriantysException.throwUnsupportedOperation();
        return null;
    }

    @Override
    public void getStudent(Color color) {
        EriantysException.throwUnsupportedOperation();
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
