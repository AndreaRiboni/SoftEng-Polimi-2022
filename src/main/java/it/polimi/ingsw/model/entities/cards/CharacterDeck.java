package it.polimi.ingsw.model.entities.cards;

import it.polimi.ingsw.model.places.GameBoard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class CharacterDeck {
    private final GameBoard gameboard;
    private final CharacterCard[] cards;

    public CharacterDeck(GameBoard gameboard) {
        this.gameboard = gameboard;
        cards = new CharacterCard[GameBoard.NOF_CHAR_CARDS];
        //inizializzare
    }

    /**
     * it returns the only active (or inactive) cards in the deck.
     * Specify "true" for the active cards, "false" otherwise.
     * A card is considered active whenever a player possesses it.
     *
     * @return (in)active cards
     */
    public CharacterCard[] getCardsByStatus(boolean status) {
        return (CharacterCard[]) Arrays.stream(cards).filter(card -> card.isOnBoard()==status).toArray();
    }

    public CharacterCard[] draw3Cards() {
        CharacterCard[] picked = new CharacterCard[3];
        for (int i = 0; i < 3; i++){
            CharacterCard[] inactive_cards = getCardsByStatus(false);
            int index = (int)(Math.random() * inactive_cards.length);
            picked[i] = inactive_cards[index];
        }
        return picked;
    }

    private void createDeck(){
        //load from json
    }


}
