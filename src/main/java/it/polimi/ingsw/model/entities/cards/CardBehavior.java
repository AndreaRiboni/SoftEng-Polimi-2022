package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.entities.Student;
import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;

public abstract class CardBehavior {
    protected GameBoard gameboard;
    protected int island_index;
    protected Color color;

    public CardBehavior(GameBoard gameboard){
        this.gameboard = gameboard;
        island_index = -1;
        color = null;
    }

    public abstract Student[] getAvailableStudents();
    public abstract void getStudent(Color color);
    public abstract void addStudent(Student student);
    public abstract void preTurnEffect();
    public abstract void postTurnEffect();

    public void chooseColor(Color color){
        this.color = color;
    }

    public void chooseIsland(int island_index) throws EriantysException {
        EriantysException.throwInvalidIslandIndex(island_index);
        this.island_index = island_index;
    }
}
