package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDeckTest {
    private GameBoard gameBoard;

    @BeforeEach
    public void init() throws EriantysException {
        gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));
    }

    @Test
    public void getActiveCardTest() throws EriantysException {
        //there are always 3 active char-card (not less, not more)
        assertNotNull(gameBoard.getActiveCharacterCard(0));
        assertNotNull(gameBoard.getActiveCharacterCard(1));
        assertNotNull(gameBoard.getActiveCharacterCard(2));
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.getActiveCharacterCard(3);
        });
    }

}