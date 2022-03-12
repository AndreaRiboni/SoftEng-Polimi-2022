package it.polimi.ingsw.model.places;

import it.polimi.ingsw.model.entities.Player;
import it.polimi.ingsw.model.entities.cards.CharacterDeck;

public class GameBoard {
    private Bag bag;
    private Cloud[] clouds;
    private Player[] players;
    private Island[] islands;
    private CharacterDeck character_cards;

    public GameBoard(int nof_players){
        bag = new Bag();
        clouds = new Cloud[2];
        players = new Player[nof_players];
        islands = new Island[12];
        character_cards = new CharacterDeck();
    }

    //metodi di interfaccia
}
