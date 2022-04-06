package it.polimi.ingsw.model.entities;

import it.polimi.ingsw.model.places.GameBoard;
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
}