package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public class MotherNatureBehavior extends CardBehavior{

    public MotherNatureBehavior(GameBoard gameboard, int id, int extra_steps, int extra_points, boolean avoid_color, boolean avoid_towers, boolean pick_island) {
        super(gameboard, id,0, 0, 0, extra_steps, extra_points, 0, pick_island, avoid_towers, avoid_color);
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
