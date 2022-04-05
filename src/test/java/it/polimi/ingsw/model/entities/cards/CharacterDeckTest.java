package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDeckTest {

    @Test
    public void getActiveCardTest() throws EriantysException {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initialize(2, (int) (Math.random() * GameBoard.NOF_ISLAND));

        assertNotNull(gameBoard.getActiveCharacterCard(0));
        assertNotNull(gameBoard.getActiveCharacterCard(1));
        assertNotNull(gameBoard.getActiveCharacterCard(2));
        IndexOutOfBoundsException thrown = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            gameBoard.getActiveCharacterCard(3);
        });
    }

}