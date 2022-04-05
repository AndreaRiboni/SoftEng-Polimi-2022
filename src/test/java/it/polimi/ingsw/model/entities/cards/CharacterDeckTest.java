package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;
import it.polimi.ingsw.model.utils.EriantysException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDeckTest {
    /*
metodo di riferimento

  public void draw3Cards() {
        int found = 0;
        do {
            int index = (int)(Math.random() * cards.length);
            if(!cards[index].isOnBoard()){
                found++;
                cards[index].setActive();
            }
        } while(found < 3);
    }


papabile test: si verifica che vengano pescate 3 carte diverse e che queste siano le uniche presenti sul tavolo (onBoard asserito)

commento after: in realtà dati gli attuali metodi di interfaccia non è possibile stabilire se queste siano effettivamente le uniche presenti,
ma di fatto a noi importa che solo queste 3 siano accessibili (di conseguenza, scegliendo una quarta carta abbiamo un'eccezione)
*/

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
        EriantysException thrown = Assertions.assertThrows(EriantysException.class, () -> {
            gameBoard.getActiveCharacterCard(3);
        });
    }

}