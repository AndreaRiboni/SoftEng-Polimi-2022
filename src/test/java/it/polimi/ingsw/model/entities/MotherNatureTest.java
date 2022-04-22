package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.places.Island;
import it.polimi.ingsw.model.utils.Color;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureTest {
    private GameBoard gameBoard;

    @BeforeEach
    public void init() throws EriantysException {
        gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));
    }

    @Test
    void stepForward() throws EriantysException {
        for(int i = 1; i < 6; i++) {
            int steps = i;
            assertDoesNotThrow(() -> gameBoard.moveMotherNature(steps));
        }
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.moveMotherNature(-1);
        });
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.moveMotherNature(0);
        });
        Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.moveMotherNature(6);
        });
    }

    @Test
    void getterAndSetter(){
        MotherNature mn = gameBoard.getMotherNature();
        mn.avoidColor(Color.RED);
        mn.avoidTowers();
        assertTrue(mn.hasToAvoidTowers());
        assertEquals(mn.hasToAvoidColor(), Color.RED);
        mn.endTurn();
        assertFalse(mn.hasToAvoidTowers());
        assertNull(mn.hasToAvoidColor());
    }

    @Test
    void walkTest() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));
        Island islands[] = gameBoard.getIslands();
        islands[2].setNext(islands[3]);
        islands[5].setNext(islands[6]);
        islands[6].setNext(islands[7]);
        MotherNature mn = gameBoard.getMotherNature();
        mn.setIslandIndex(0);
        mn.stepForward(4);
        assertEquals(5, mn.getIslandIndex());
        mn.stepForward(1);
        assertEquals(8, mn.getIslandIndex());
    }
}