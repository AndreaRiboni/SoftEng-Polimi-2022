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
        gameBoard.setNOFPlayers(2);
        gameBoard.initializeMotherNature((int) (Math.random() * GameBoard.NOF_ISLAND));
        gameBoard.initializeCharacterDeck(); //metodo da testare
        gameBoard.initalizePlayers();

        assertTrue(gameBoard.getActiveCharacterCard(0) != null);
        assertTrue(gameBoard.getActiveCharacterCard(1) != null);
        assertTrue(gameBoard.getActiveCharacterCard(2) != null);
        IndexOutOfBoundsException thrown = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            gameBoard.getActiveCharacterCard(3);
        });
    }

}